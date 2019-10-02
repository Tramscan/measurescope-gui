long duration;
void setup() {
  // put your setup code here, to run once:
  pinMode(13, OUTPUT);
  Serial.begin(9600);
  ledBlink();
  delay(5000);

}
//USE JSSC
void loop() {
  // put your main code here, to run repeatedly:
  String serialMat="G0 X100";
  while(Serial.available()>0){
    
    if(Serial.readString() == serialMat){
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

