#synctime - synchronize your chronos watch to your computer
#
#
# Copyright (c) 2010 Sean Brewer 
#
# Permission is hereby granted, free of charge, to any person
# obtaining a copy of this software and associated documentation
# files (the "Software"), to deal in the Software without
# restriction, including without limitation the rights to use,
#
# copies of the Software, and to permit persons to whom the
# Software is furnished to do so, subject to the following
# conditions:
#
# The above copyright notice and this permission notice shall be
# included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
# OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
# NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
# HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
# WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
# FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
# OTHER DEALINGS IN THE SOFTWARE.
#
#
#You can contact me at seabre986@gmail.com or on reddit (seabre) if you want.
#
#****TONS**** of mistakes in the last version. Sorry about that everybody.

import serial
import array
import time



class Serial:
    def __init__(self,serialPort):
        self.__ser = serial.Serial(serialPort,115200,timeout=None)
    
    
    def __splitIntoNPieces(self, s,n):
        return [s[i:i+n] for i in range(0, len(s), n)]
    
    def __makeByteString(self, arr):
        return array.array('B', arr).tostring()
    
    def __startAccessPoint(self):
        return self.__makeByteString([0xFF, 0x07, 0x03])
        
    
    def __stopAccessPoint(self):
        return self.__makeByteString([0xFF, 0x09, 0x03])
    
    
    
    def __setSyncMode(self):
        return self.__makeByteString([0xFF, 0x30, 0x03])
    def __getDataCmd(self):
        return self.__makeByteString([0xFF, 0x31, 0x04, 0x05])
    def __getStatusCmd(self):
        return self.__makeByteString([0xFF, 0x31, 0x04, 0x02])
    def __getDataStatus(self):
        return self.__makeByteString([0xFF, 0x32, 0x03])
    def __getReqData(self):
        return self.__makeByteString([0xFF, 0x33, 0x03])
    
    
    
    
    
    
    def syncTimeDateTempAlt(self, hour,minute,second,month,day,year,tempCelcius,altMeters):
        adjHour = hour + 0x80 #assumes 24h based time entry
        adjYear = year - 0x700
        adjTempCelcius = tempCelcius * 0x0A
        print hour
        cmd = [0xFF, 0x31, 0x16, 0x03,adjHour,minute,second,0x07,adjYear,month,day,0x06,0x1E]
    
        hexCelcius = hex(adjTempCelcius)[2:].zfill(4)
        hexMeters = hex(altMeters)[2:].zfill(4)
    
        for i in self.__splitIntoNPieces(hexCelcius,2):
            cmd.append(int(i,16))
        for i in self.__splitIntoNPieces(hexMeters,2):
            cmd.append(int(i,16))
        
        for i in xrange(0,5):
            cmd.append(0)
       
        return self.__makeByteString(cmd)
    
    def startAP(self):
        print "starte AP"
        self.__ser.write(self.__startAccessPoint())
        return self.readResponse()
    
    def stopAP(self):
        self.__ser.write(self.__stopAccessPoint())
        return self.readResponse()
    
    
    def startDataSync(self):
        self.__ser.write(self.__setSyncMode())
        return self.readResponse()[2] == 0x02 # 0x02
    
    def getAmount(self):
        self.__ser.write(self.__getStatusCmd())
        data = self.readResponse()
        if not data[1] == 0x06:
            raise Exception("Fehler beim AmountCmd")
        
        time.sleep(1)
        
        self.__ser.write(self.__getDataStatus())
        data = self.readResponse()
        if not data[3] == 0x01:
            raise Exception("not Read 2 Read")
            
        
        self.__ser.write(self.__getReqData())
        data = self.readResponse()
        ''' if not data[4] == 0x55:
            raise Exception("unknown data")
        '''        
        return (data[12] << 8) | data[13];        
    
    def getData(self):
        self.__ser.write(self.__getDataCmd())
        data = self.readResponse()
        if not data[1] == 0x06:
            raise Exception("Fehler beim DataCmd")
        
        time.sleep(1)
        
        self.__ser.write(self.__getDataStatus())
        data = self.readResponse()
        if not data[3] == 0x01:
            raise Exception("not Read 2 Read")
            
        
        
        self.__ser.write(self.__getReqData())
        data = self.readResponse()
        ''' if not data[4] == 0x55:
            raise Exception("unknown data")
        '''
        return data[5:]        
        
            
    
    
        
        
    def readResponse(self):
        ret = array.array("B", self.__ser.read(3))
        
                
        if not ret[0] == 0xFF:
            print "FUCK"
            return [0x00]
        print "status %02X" % (ret[1])
        print "Bytes %02X" % (ret[2])
        if ret[2] > 0x03:
            # Lese die restlichen Bytes
            ret2 = array.array("B", self.__ser.read(ret[2]-0x03))
            print "DATA"
            for x in ret2:
                print "%02X" % (x)
            ret.extend(ret2)
                    
        return ret  
    
def b2H( byteStr ):
    return ''.join( [ "%02X " % ord( x ) for x in byteStr ] ).strip()      

def h2B (hexStr):
    bytes = []
    hexStr = ''.join( hexStr.split(" "))
    for i in range(0, len(hexStr), 2):
        bytes.append( chr(int (hexStr[i:i+2],16)))
    return ''.join(bytes)

        
