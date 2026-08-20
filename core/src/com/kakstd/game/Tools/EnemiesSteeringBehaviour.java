package com.kakstd.game.Tools;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.kakstd.game.SubmarineGame;

public class EnemiesSteeringBehaviour implements Steerable<Vector2> {
    Body body;
    boolean tagged;
    float boundingRadius;
    float linearSpeed;
    float maxLinearSpeed, maxLinearAcceleration;
    float maxAngularSpeed, maxAngularAcceleration;
    SteeringBehavior<Vector2> behavior;
    SteeringAcceleration<Vector2> steeringOutput;
    public EnemiesSteeringBehaviour(Body body, float boundingRadius){
        this.body = body;
        this.boundingRadius = boundingRadius;
        this.tagged = false;
        this.maxLinearAcceleration = 2f/SubmarineGame.PPM;
        this.steeringOutput = new SteeringAcceleration<>(new Vector2());
        this.body.setUserData(this);
    }
    public void update(float dt){
        if(behavior != null){
            behavior.calculateSteering(steeringOutput);
            applySteering(dt);
        }
    }
    private void applySteering(float dt){
        boolean anyAcceleration = false;
        if(!steeringOutput.linear.isZero()){
            Vector2 force = steeringOutput.linear.nor().scl(linearSpeed);
            body.applyLinearImpulse(force, body.getWorldCenter(), true);
            anyAcceleration = true;

        }
        // -------------------- FIXTURE ROTATION + MOVEMENT--------------------
        if (steeringOutput.angular != 0) {
            // this method internally scales the torque by deltaTime
            body.applyTorque(steeringOutput.angular, true);
            anyAcceleration = true;
        }else {
            // If we haven't got any velocity, then we can do nothing.
            Vector2 linVel = getLinearVelocity();
            if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
                float newOrientation = vectorToAngle(linVel);
                body.setAngularVelocity((newOrientation - getAngularVelocity()) * dt); // this is superfluous if independentFacing is always true
                body.setTransform(body.getPosition(), newOrientation);
            }
        }
        //------------------------------------------------------------------
        if(anyAcceleration){
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSqrt = velocity.len2();
            if(currentSpeedSqrt > maxLinearSpeed*maxLinearSpeed){
                body.setLinearVelocity(velocity.scl((maxLinearSpeed/(float) Math.sqrt(currentSpeedSqrt))));
            }
        }
    }
    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxLinearSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxLinearAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {

    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return SteeringUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return SteeringUtils.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return null;
    }
    public Body getBody(){
        return body;
    }
    public void setBehavior(SteeringBehavior<Vector2> behavior){
        this.behavior = behavior;
    }
    public void setLinearSpeed(float linearSpeed){
        this.linearSpeed = linearSpeed;
    }

    public SteeringBehavior<Vector2> getBehavior(){
        return behavior;
    }
}
