package com.danosoftware.galaxyforce.sprites.refactor;

import android.util.Log;

import com.danosoftware.galaxyforce.enumerations.BaseMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.sprites.game.implementations.BaseHelper;
import com.danosoftware.galaxyforce.sprites.game.interfaces.Life;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteBase;

public class AbstractBaseSprite extends AbstractMovingSprite implements IBaseSprite {

    @Override
    public void animate() {

    }

    @Override
    public void onHitBy(IAlien alien) {

    }

    @Override
    public void onHitBy(IAlienMissile missile) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void collectPowerUp(PowerUpType powerUpType) {
        activatePowerUp(powerUpType);
    }

    /**
     * activate the supplied power up type by calling the appropriate method.
     *
     * @param powerUpType
     */
    private void activatePowerUp(PowerUpType powerUpType)
    {
        Log.i(TAG, "Power-Up: '" + powerUpType.name() + "'.");

        switch (powerUpType)
        {
            case ENERGY:

                // add energy to base
                if (primaryBase.isActive())
                {
                    primaryBase.addEnergy(2);
                }

                // update energy bar
                energy = energyBar.getEnergyBar();
                reBuildSprites = true;

                break;

            case LIFE:

                // add extra life
                lives++;

                // update lives
                lifeSprites = Life.getLives(lives);
                reBuildSprites = true;

                break;

            case MISSILE_BLAST:

                // add blast missile for set time
                if (primaryBase.isActive())
                {
                    primaryBase.setBaseMissileType(BaseMissileType.BLAST, 0.5f, 10f);
                }

                break;

            case MISSILE_FAST:

                // add fast missile for set time
                if (primaryBase.isActive())
                {
                    primaryBase.setBaseMissileType(BaseMissileType.FAST, 0.2f, 10f);
                }

                break;

            case MISSILE_GUIDED:

                // add guided missile for set time
                if (primaryBase.isActive())
                {
                    primaryBase.setBaseMissileType(BaseMissileType.GUIDED, 0.5f, 10f);
                }

                break;

            case MISSILE_LASER:

                // add laser missile for set time
                if (primaryBase.isActive())
                {
                    primaryBase.setBaseMissileType(BaseMissileType.LASER, 0.5f, 10f);
                }

                break;

            case MISSILE_PARALLEL:

                // add parallel missile for set time
                if (primaryBase.isActive())
                {
                    primaryBase.setBaseMissileType(BaseMissileType.PARALLEL, 0.5f, 10f);
                }

                break;

            case MISSILE_SPRAY:

                // add spray missile for set time
                if (primaryBase.isActive())
                {
                    primaryBase.setBaseMissileType(BaseMissileType.SPRAY, 0.5f, 10f);
                }

                break;

            case SHIELD:

                // add shield to primary base for set time

                if (primaryBase.isActive())
                {
                    // tell base it has a shield for set time. base will call back
                    // with shield sprites and also when the timer expires.
                    primaryBase.addShield(10f, 0f);
                }

                break;

            case HELPER_BASES:

                // if only one base then add helper bases
                if (primaryBase.isActive())
                {
                    /*
                     * clear any previous helper bases - new bases will be added
                     * with full energy. also replaces any previously destroyed
                     * helper bases.
                     */
                    for (SpriteBase eachBase : bases)
                    {
                        // SpriteBase eachBase = baseList.next();
                        if (eachBase != primaryBase)
                        {
                            // remove current helper base
                            eachBase.setDestroyed();
                        }
                    }

                    /*
                     * add helper base offset to left/right of primary base
                     */
                    SpriteBase helperBaseLeft = BaseHelper.newBase(primaryBase, this, -64);
                    bases.add(helperBaseLeft);

                    SpriteBase helperBaseRight = BaseHelper.newBase(primaryBase, this, +64);
                    bases.add(helperBaseRight);

                    reBuildSprites = true;
                }

                break;

            default:
                throw new IllegalArgumentException("Unsupported Power Up Type: '" + powerUpType.name() + "'.");
        }
    }

}
