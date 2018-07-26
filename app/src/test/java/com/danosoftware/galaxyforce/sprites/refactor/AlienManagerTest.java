package com.danosoftware.galaxyforce.sprites.refactor;

import android.util.Log;

import com.danosoftware.galaxyforce.waves.SubWave;
import com.danosoftware.galaxyforce.waves.managers.WaveManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
        };

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
    public void shouldGetNextSubWaveWhenAllDestroyedAndMoreSubWaves() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        when(mockAlien.isDestroyed()).thenReturn(true);
        when(mockWaveMgr.hasNext()).thenReturn(true);
        alienMgr.animate(0);

        // waveMgr.next() is called twice.
        // Once on setUp() and again on for next sub-wave.
        verify(mockWaveMgr, times(2)).next();
    }
}
