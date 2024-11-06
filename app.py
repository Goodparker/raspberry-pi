from flask import Flask
import RPi.GPIO as GPIO

app = Flask(__name__)

# Настройка GPIO
LIGHT_PIN = 18  # Укажите номер пина
GPIO.setmode(GPIO.BCM)
GPIO.setup(LIGHT_PIN, GPIO.OUT)

@app.route('/light/<state>', methods=['GET'])
def toggle_light(state):
    if state == 'on':
        GPIO.output(LIGHT_PIN, GPIO.HIGH)
    elif state == 'off':
        GPIO.output(LIGHT_PIN, GPIO.LOW)
    return '', 204

if __name__ == '__main__':
    try:
        app.run(host='0.0.0.0', port=5000)
    except KeyboardInterrupt:
        GPIO.cleanup()
