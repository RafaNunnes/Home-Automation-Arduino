#include <IRremote.h>   // Adiciona a biblioteca do Infravermelho no algoritmo 
IRsend irsend;
boolean zero_crossing_enabled = false;
int logical_port_dimmer;  // Pino digital definido como controlador do MOC3011
volatile int dimming = 120; // O dimmer começa com a menor potência fornecida, ou seja, com a carga fora de funcionamento

byte control_type_received, logical_port_received;
byte format_type_received, num_bits_received;
byte content_dimmer;

void setup()
{
  Serial.begin(9600); //Iniciando a Serial
  delay(1000); // Espera um tempo de 1 segundo.
  pinMode(8, OUTPUT); // Define o pino digital 8 do Arduino como Saída.
  pinMode(7, OUTPUT); // Define o pino digital 7 do Arduino como Saída.
  pinMode(6, OUTPUT); // Define o pino digital 6 do Arduino como Saída.
  pinMode(5, OUTPUT); // Define o pino digital 5 do Arduino como Saída.
  attachInterrupt(0, zeroCrossing, RISING); //Inicio da interrupção quando a carga está conectada à rede doméstica.
}

void loop()
{
  while(!Serial.available()); // O programa espera receber alguma variável pela serial que será enviada pelo bluetooth
  
  if(Serial.available())
  {
    control_type_received = Serial.read();
    delay(20);
  }
    
  if(Serial.available())
  {
    logical_port_received = Serial.read();
    delay(20);
  }

  Serial.print("TIPO CONTROLE:  ");
  Serial.println(control_type_received);
  Serial.print("PORTA LÓGICA:   ");
  Serial.println(logical_port_received);

  switch(control_type_received)
  {
    case 3:   // Código de ativação do circuito Infravermelho
      unsigned long infrared_code;
      
      if(Serial.available())
      {
        format_type_received = Serial.read();
      }

      if(Serial.available())
      {
        num_bits_received = Serial.read();
      }
      
      infrared_code = getStringInput().toInt();
    
      infraredCircuit(infrared_code, format_type_received, num_bits_received);
      break;   
      
    case 1:   // Código de ativação do circuito Chaveado
      switchedCircuit(logical_port_received);
      break;
      
    case 2:   // Código de ativação do circuito Dimmer
      byte dimmer_value = 0;
      while(true)
      {
        while(!Serial.available()); // O programa espera receber alguma variável pela serial que será enviada pelo bluetooth
        
        if(Serial.available())
        {
          dimmer_value = Serial.read();
        }
        
        if(dimmer_value == 255)
        {
          break;
        }

        dimmerCircuit(logical_port_received, dimmer_value);
        Serial.println(dimmer_value);
      }
      break;   
  }
  
}

String getStringInput()
{
  String content_serial = "";
  char in_data;
  
  while(Serial.available() > 0)
  {
    in_data = Serial.read();
  
    if(in_data != '\n')
    {
      content_serial.concat(in_data);
    }
    delay(10);
  }

  return content_serial;
}

void switchedCircuit(byte logical_port)
{
  digitalWrite(logical_port,!digitalRead(logical_port));
}

void dimmerCircuit(byte logical_port, byte content)
{
  int remap = map (content, 0, 128 , 120, 0);  // Simula um potenciômetro e inverte a sequência dos números 
  delay(1);

  /*if(remap <= 100){                 
     dimming = remap;
     
     logical_port_dimmer = logical_port;
     delay(1);     
     zero_crossing_enabled = true;       //  Habilita o cálculo de passagem por zero da rede
  }
  else{                                                           
      digitalWrite(logical_port, LOW);
      delay(1);
      zero_crossing_enabled = false;     // Desabilita o cálculo de passagem por zero da rede
  }*/
  
  if(remap > 100){                                                           
      digitalWrite(logical_port, LOW);
      delay(1);
      zero_crossing_enabled = false;     // Desabilita o cálculo de passagem por zero da rede
    }
  else if(remap <= 100){                 
     dimming = remap;
     
     logical_port_dimmer = logical_port;
     delay(1);     
     zero_crossing_enabled = true;       //  Habilita o cálculo de passagem por zero da rede
 }
}

void infraredCircuit(unsigned long code, int format_type, byte num_bits)
{
  /****************************
   * Format Types: 
   * Value: 127 ---> SAMSUNG
   * Value: 126 ---> NEC
   * Value: 125 ---> SONY
   * Value: 124 ---> RC5
   * Value: 123 ---> RC6
   * Value: 122 ---> UNKNOWN
   ****************************/
   
  Serial.println("Configurações");
  Serial.println(code);
  Serial.println(format_type);
  Serial.println(num_bits);
  
  switch(format_type)
  {
    case 127:
      irsend.sendSAMSUNG(code, num_bits);
      delay(100);
      break;
      
    case 126:
      irsend.sendNEC(code, num_bits);
      delay(100);
      break;
      
    case 125:
      irsend.sendSony(code, num_bits);
      delay(100);
      break;
      
    case 124:
      irsend.sendRC5(code, num_bits);
      delay(100);
      break;
      
    case 123:
      irsend.sendRC6(code, num_bits);
      delay(100);
      break;

    case 122:
      Serial.println("Formato desconhecido!");
      break;  
  }
}

void zeroCrossing() // Código para Funcionamento do Dimmer
{
  if(zero_crossing_enabled)
  {
    int dimtime = (65 * dimming);  // Após passar o tempo calculado para o ângulo de disparo   
    delayMicroseconds(dimtime);    // O algoritmo envia um pulso de 8,33 microssegundos para o gate do TRIAC 
    digitalWrite(logical_port_dimmer, HIGH);
    delayMicroseconds(8.33);        
    digitalWrite(logical_port_dimmer, LOW);  
  } 
}
