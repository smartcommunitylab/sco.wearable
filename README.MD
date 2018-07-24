# Viaggia Play&Go (Wearable Communication)
## Communication Wearable ⟶ Phone
```
sender_activity_value
```
## Communication Phone ⟶ Wearable
```
sender_activity_mode_value
```
## Application
### Login screen
<img src="https://gitlab.com/davide-calza/viaggia-wearable-android/raw/master/screens/login.png" width="200" height="200" />
1. **Wearable:** wearable_logstatus_null
2. **Phone:** 
    * smartphone_logstatus_ok_ok
    * smartphone_logstatus_notlogged_notlogged

### Start Tracking screen
<img src="https://gitlab.com/davide-calza/viaggia-wearable-android/raw/master/screens/track-1.png" width="200" height="200" />
<img src="https://gitlab.com/davide-calza/viaggia-wearable-android/raw/master/screens/track-2.png" width="200" height="200" />
<img src="https://gitlab.com/davide-calza/viaggia-wearable-android/raw/master/screens/user-1.png" width="200" height="200" />
<img src="https://gitlab.com/davide-calza/viaggia-wearable-android/raw/master/screens/user-2.png" width="200" height="200" />
1. **Wearable:** 
    * wearable_starttracking_[bike|bus|train|walk]
    * wearable_getinfo_[username|points|ranking]
2. **Phone:** 
    * smartphone_starttracking_[bike|bus|train|walk]_[ok|err]
    * smartphone_getinfo_[username|points|ranking]_[username|points|ranking]

### On Tracking screen
<img src="https://gitlab.com/davide-calza/viaggia-wearable-android/raw/master/screens/tracking-on.png" width="200" height="200" />
1. **Wearable:** 
    * wearable_stoptracking_[bike|bus|train|walk]
    * wearable_getdistance_[bike|bus|train|walk]
2. **Phone:** 
    * wearable_stoptracking_[bike|bus|train|walk]_[ok|err]
    * wearable_getdistance_[bike|bus|train|walk]_distance

### Sync Tracking screen
<img src="https://gitlab.com/davide-calza/viaggia-wearable-android/raw/master/screens/tracking-sync.png" width="200" height="200" />
2. **Phone:** 
    * smartphone_sync_ok_ok
    * smartphone_sync_err_message