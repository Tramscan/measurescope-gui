long duration;
void setup() {
  pinMode(13, OUTPUT);
  Serial.begin(9600);
  ledBlink();
  delay(5000);

}
//USE JSSC
void loop() {
  String testSendString="G0 X100";
  while(Serial.available()>0){
    
    if(Serial.readString() == testSendString){
      ledBlink();
    }

  }
  

}

void ledBlink(){
  for(int i =0;i<6;i++){
  digitalWrite(13,HIGH);
  delay(100);
  digitalWrite(13,LOW);
  delay(100);
  }
  digitalWrite(13,LOW);
}

