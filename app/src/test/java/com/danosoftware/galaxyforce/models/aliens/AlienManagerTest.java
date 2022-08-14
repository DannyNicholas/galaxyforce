package com.danosoftware.galaxyforce.models.aliens;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import android.util.Log;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.services.achievements.AchievementService;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.IResettableAlien;
import com.danosoftware.galaxyforce.sprites.providers.GamePlaySpriteProvider;
import com.danosoftware.galaxyforce.waves.SubWave;
import com.danosoftware.galaxyforce.waves.managers.WaveManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class AlienManagerTest {

  private AlienManager alienMgr;
  private WaveManager mockWaveMgr;
  private IResettableAlien mockAlien;
  private GamePlaySpriteProvider spriteprovider;

  private static final int ALIEN_COUNT = 10;

  @Captor
  private ArgumentCaptor<List<IAlien>> argumentCaptor;

  @Before
  public void setUp() {
    // mock any static android logging
    mockStatic(Log.class);

    mockAlien = mock(IResettableAlien.class);
    when(mockAlien.isActive()).thenReturn(true);
    when(mockAlien.isVisible()).thenReturn(true);
    when(mockAlien.isDestroyed()).thenReturn(false);
    when(mockAlien.isEndOfPass()).thenReturn(false);
    when(mockAlien.x()).thenReturn((float) GameConstants.SCREEN_MID_X);
    when(mockAlien.y()).thenReturn((float) GameConstants.SCREEN_MID_Y);
    when(mockAlien.halfHeight()).thenReturn(50);
    when(mockAlien.halfWidth()).thenReturn(50);

    List<IAlien> aliens = new ArrayList<>();
    for (int i = 0; i < ALIEN_COUNT; i++) {
      aliens.add(mockAlien);
    }

    SubWave subWave = mock(SubWave.class);
    when(subWave.isWaveRepeated()).thenReturn(true);
    when(subWave.getAliens()).thenReturn(aliens);

    mockWaveMgr = mock(WaveManager.class);
    when(mockWaveMgr.isWaveReady()).thenReturn(true);
    when(mockWaveMgr.hasNext()).thenReturn(true);
    when(mockWaveMgr.next()).thenReturn(subWave);

    AchievementService achievements = mock(AchievementService.class);
    spriteprovider = mock(GamePlaySpriteProvider.class);

    alienMgr = new AlienManager(mockWaveMgr, achievements, spriteprovider);
    alienMgr.isWaveReady();
  }

  @Test
  public void shouldReturnActiveAliens() {
    List<IAlien> activeAliens = alienMgr.activeAliens();
    alienMgr.animate(0);
    assertThat(activeAliens.size(), is(ALIEN_COUNT));
  }

  @Test
  public void shouldProvideAllVisibleAliens() {
    alienMgr.animate(0);

    // sprite provider will only be called once
    // with original aliens on initialisation.
    // the animation cycle should not send any more aliens
    // since nothing will change.
    verify(spriteprovider, times(1)).setAliens(argumentCaptor.capture());
    List<IAlien> capturedAliens = argumentCaptor.getValue();
    assertThat(capturedAliens.size(), is(ALIEN_COUNT));
  }

  @Test
  public void shouldReturnNoActiveAliens() {
    when(mockAlien.isActive()).thenReturn(false);
    alienMgr.animate(0);
    List<IAlien> activeAliens = alienMgr.activeAliens();
    assertThat(activeAliens.size(), is(0));
  }

  // check the expected visible aliens are sent to the sprite provider
  @Test
  public void shouldProvideNoVisibleAliens() {
    // set-up mock so alien is initially visible and then invisible
    when(mockAlien.isVisible()).thenReturn(true, false);
    alienMgr.animate(0);

    // sprite provider will be called twice.
    // 1st time with original aliens on initialisation.
    // 2nd time with no aliens once all are set to invisible.
    verify(spriteprovider, times(2)).setAliens(argumentCaptor.capture());
    List<List<IAlien>> capturedAliens = argumentCaptor.getAllValues();
    assertThat(capturedAliens.get(0).size(), is(ALIEN_COUNT));
    assertThat(capturedAliens.get(1).size(), is(0));
  }

  @Test
  public void shouldCompleteWaveWhenAllDestroyedAndNoNewSubWaves() {
    when(mockAlien.isDestroyed()).thenReturn(true);
    when(mockWaveMgr.hasNext()).thenReturn(false);
    alienMgr.animate(0);
    assertThat(alienMgr.isWaveComplete(), is(true));
  }

  @Test
  public void shouldResetAliensAtEndOfPass() {
    when(mockAlien.isEndOfPass()).thenReturn(true);
    when(mockWaveMgr.hasNext()).thenReturn(false);
    alienMgr.animate(0);
    verify(mockAlien, times(ALIEN_COUNT)).reset(any(float.class));
  }

  @Test
  public void shouldGetNextSubWaveWhenAllDestroyedAndMoreSubWaves() {
    when(mockAlien.isDestroyed()).thenReturn(true);
    when(mockWaveMgr.hasNext()).thenReturn(true);
    alienMgr.animate(0);

    // waveMgr.next() is called twice.
    // Once on setUp() and again on for next sub-wave.
    verify(mockWaveMgr, times(2)).next();
  }

  @Test
  public void shouldReturnNullIfSelectingFromNoActiveAliens() {
    // destroy all mocked aliens
    for (IAlien alien : alienMgr.activeAliens()) {
      when(alien.isActive()).thenReturn(false);
    }
    alienMgr.animate(0f);

    IAlien selectedAlien = alienMgr.chooseActiveAlien();
    assertThat(selectedAlien, is(nullValue()));
  }

  @Test
  public void shouldSelectARandomAlien() {
    IAlien alien = alienMgr.chooseActiveAlien();
    assertThat(alien, is(not(nullValue())));
  }

  @Test
  public void spawnAlienTest() {
    // when animate is called on an alien, call the spawn alien method
    IAlien spawnedAlien = mock(IAlien.class);
    when(spawnedAlien.isVisible()).thenReturn(true);
    final List<IAlien> spawnedAliens = Collections.singletonList(spawnedAlien);
    doAnswer(invocation -> {
      alienMgr.spawnAliens(spawnedAliens);
      return null;
    }).when(mockAlien).animate(any(Float.class));

    // first animation loop will queue up the spawned aliens
    alienMgr.animate(0);

    // spawned aliens will be added in second loop
    alienMgr.animate(0);

    // sprite provider will be called twice.
    // 1st time with original aliens on initialisation.
    // 2nd time with original aliens plus spawned aliens.
    verify(spriteprovider, times(2)).setAliens(argumentCaptor.capture());
    List<List<IAlien>> capturedAliens = argumentCaptor.getAllValues();
    assertThat(capturedAliens.get(0).size(), is(ALIEN_COUNT));
    assertThat(capturedAliens.get(1).size(), is(ALIEN_COUNT * 2));
  }
}
