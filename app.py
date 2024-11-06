from flask import Flask
import RPi.GPIO as GPIO
import time

app = Flask(__name__)

LIGHT_PIN = 18
SERVO_PIN = 17  
GPIO.setmode(GPIO.BCM)
GPIO.setup(LIGHT_PIN, GPIO.OUT)
GPIO.setup(SERVO_PIN, GPIO.OUT)

@app.route('/light/<state>', methods=['GET'])
def toggle_light(state):
    if state == 'on':
        GPIO.output(LIGHT_PIN, GPIO.HIGH)
    elif state == 'off':
        GPIO.output(LIGHT_PIN, GPIO.LOW)
    return '', 204

@app.route('/servo/<state>', methods=['GET'])
def toggle_servo(state):
    if state == 'on':
        GPIO.output(SERVO_PIN, GPIO.HIGH) 
        time.sleep(3)                       
        GPIO.output(SERVO_PIN, GPIO.LOW)   
    return '', 204

if __name__ == '__main__':
    try:
        app.run(host='0.0.0.0', port=5000)
    except KeyboardInterrupt:
        GPIO.cleanup()
