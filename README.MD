# Viaggia Play&Go (Wearable Communication)
## Communication Wearable ⟶ Phone
'''
sender_activity_value
'''
## Communication Phone ⟶ Wearable
'''
sender_activity_mode_value
'''
## Application
### Login screen
![Login screen image](https://drive.google.com/open?id=1cOI86Y5TK1ODJJMakQ_S8NutYRXiGJFQ)
1. **Wearable:** wearable_logstatus_null
2. **Phone:** 
    * smartphone_logstatus_ok_ok
    * smartphone_logstatus_notlogged_notlogged

### Start Tracking screen
![Login screen image](https://drive.google.com/open?id=1K7AN9zdZvT0mnMclfKU5nxflpOhPa7Iz)
![Login screen image](https://drive.google.com/open?id=1Jh4XP8E5LEsYGnGwsv8etdiydtcte3mh)
1. **Wearable:** 
    * wearable_starttracking_[bike|bus|train|walk]
    * wearable_getinfo_[username|points|ranking]
2. **Phone:** 
    * smartphone_starttracking_[bike|bus|train|walk]_[ok|err]
    * smartphone_getinfo_[username|points|ranking]_[username|points|ranking]

### On Tracking screen
![Login screen image](https://drive.google.com/open?id=1Iy9HsSXtpoccES8xhdG9_XIGb65EqOsw)
1. **Wearable:** 
    * wearable_stoptracking_[bike|bus|train|walk]
    * wearable_getdistance_[bike|bus|train|walk]
2. **Phone:** 
    * wearable_stoptracking_[bike|bus|train|walk]_[ok|err]
    * wearable_getdistance_[bike|bus|train|walk]_distance

### Sync Tracking screen
![Login screen image](https://drive.google.com/open?id=18oGJIFrNT2x22DfRG7FykmltBYVyKZuv)
2. **Phone:** 
    * smartphone_sync_ok_ok
    * smartphone_sync_err_message