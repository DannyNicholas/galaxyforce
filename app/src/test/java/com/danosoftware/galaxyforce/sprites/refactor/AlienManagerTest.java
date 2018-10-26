package com.danosoftware.galaxyforce.sprites.refactor;

import android.util.Log;

import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlienWithPath;
import com.danosoftware.galaxyforce.waves.SubWave;
import com.danosoftware.galaxyforce.waves.managers.WaveManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class,})
public class AlienManagerTest {

    private AlienManager alienMgr;
    private WaveManager mockWaveMgr;
    private List<IAlien> aliens;
    private IAlienWithPath mockAlien;

    private static final int ALIEN_COUNT = 10;

    @Before
    public void setUp() {
        // mock any static android logging
        mockStatic(Log.class);

        mockAlien = mock(IAlienWithPath.class);
        when(mockAlien.isActive()).thenReturn(true);
        when(mockAlien.isVisible()).thenReturn(true);
        when(mockAlien.isDestroyed()).thenReturn(false);
        when(mockAlien.isEndOfPass()).thenReturn(false);

        aliens = new ArrayList<>();
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

        alienMgr = new AlienManager(mockWaveMgr);
        alienMgr.isWaveReady();
    }

    @Test
    public void shouldReturnActiveAliens() {
        List<IAlien> activeAliens = alienMgr.activeAliens();
        alienMgr.animate(0);
        assertThat(activeAliens.size(), is(ALIEN_COUNT));
    }

    @Test
    public void shouldReturnAllVisibleAliens() {
        List<IAlien> allAliens = alienMgr.allAliens();
        alienMgr.animate(0);
        assertThat(allAliens.size(), is(ALIEN_COUNT));
    }

    @Test
    public void shouldReturnNoActiveAliens() {
        when(mockAlien.isActive()).thenReturn(false);
        alienMgr.animate(0);
        List<IAlien> activeAliens = alienMgr.activeAliens();
        assertThat(activeAliens.size(), is(0));
    }

    @Test
    public void shouldReturnNoVisibleAliens() {
        when(mockAlien.isVisible()).thenReturn(false);
        alienMgr.animate(0);
        List<IAlien> activeAliens = alienMgr.allAliens();
        assertThat(activeAliens.size(), is(0));
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
        // destroy all aliens
        for (IAlien alien : alienMgr.activeAliens()) {
            alien.destroy();
        }

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
        final List<IAlien> spawnedAliens = Arrays.asList(mock(IAlien.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                alienMgr.spawnAliens(spawnedAliens);
                return null;
            }
        }).when(mockAlien).animate(any(Float.class));


        // confirm that the animate cycle successfully allows existing aliens to spawns new aliens
        alienMgr.animate(0);

        // confirm every alien has spawned a new alien
        //TODO this may throw a concurrent exception in which case the alien manager
        // should queue spawned aliens and add them at end of animation loop
        assertThat(alienMgr.allAliens().size(), equalTo(20));
    }
}