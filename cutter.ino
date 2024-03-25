#include "WiFi.h"
#include <HTTPClient.h>

#define PIN_A 34
#define PIN_B 35

long unsigned previousTime;
long unsigned currentTime;

bool forwarding = false;
bool backwarding = false;

bool prevA = false;
bool prevB = false;
long unsigned prevA_time = 0;
long unsigned prevB_time = 0;

long unsigned cut_time_tmp = 0;
long unsigned cut_time_A = 0;
long unsigned cut_time_B = 0;

long unsigned previousMillis = 0;
int interval = 5000;

const char* ssid = "moto g 5G plus 6966";
const char* password = "22222222";

const char* serverName = "http://192.168.103.100:3000/taglio";

void initWiFi() {
    WiFi.mode(WIFI_STA);
    WiFi.begin(ssid, password);
    
    Serial.print("Connecting to WiFi ..");

    while (WiFi.status() != WL_CONNECTED) {
        Serial.print('.');
        delay(1000);
    }

    Serial.println(WiFi.localIP());
}

void setup() {
    Serial.begin(9600);

    // setup pins for 'forward' and 'backward' signals
    pinMode(PIN_A, INPUT);
    pinMode(PIN_B, INPUT);

    // connect to hotspot wifi
    initWiFi();
}

void loop() {
    long unsigned currentMillis = millis();
    if((WiFi.status() != WL_CONNECTED) && (currentMillis - previousMillis >= interval)) {
        Serial.print(millis());
        Serial.println("Reconnecting to WiFi...");
        WiFi.disconnect();
        WiFi.reconnect();
        previousMillis = currentMillis;
        return;
    }

    bool value_A = (digitalRead(PIN_A) == 1) ? true : false;
    bool value_B = (digitalRead(PIN_B) == 1) ? true : false;

    if(value_A != prevA) {
        prevA = value_A;
        prevA_time = millis();
    }

    if(value_A == true && (millis() - prevA_time > 1000) && forwarding == false) {
        prevA = value_A;
        Serial.println("AVANTI START");
        cut_time_tmp = millis();
        forwarding = true;
    }

    if(value_A == false && forwarding == true) {
        prevA = value_A;
        cut_time_A = (millis() - cut_time_tmp);
        forwarding = false;
        Serial.print("AVANTI STOP ");
        Serial.println(cut_time_A);
    }

    if(value_B != prevB) {
        prevB = value_B;
        prevB_time = millis();
    }

    if(value_B == true && (millis() - prevB_time > 1000) && backwarding == false) {
        prevB = value_B;
        cut_time_tmp = millis();
        Serial.println("INDIETRO START");
        backwarding = true;
    }

    if(value_B == false && backwarding == true) {
        prevB = value_B;
        cut_time_B = (millis() - cut_time_tmp);
        backwarding = false;

        Serial.print("INDIETRO STOP ");
        Serial.println(cut_time_B);

        // RESULTS
        double cut_minutes = (cut_time_A + cut_time_B) / 60000;
        Serial.println("NUOVO TAGLIO, TEMPO COMPLESSIVO: " + String(cut_minutes, 4));

        if(WiFi.status()== WL_CONNECTED){
            WiFiClient client;
            HTTPClient http;

            http.begin(client, serverName);
            http.addHeader("Content-Type", "application/x-www-form-urlencoded");
            String httpRequestData = "value=" + String(cut_minutes, 4);
            http.POST(httpRequestData);
            http.end();
        }
    }
}