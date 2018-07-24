var app = {
  initialize: function () {
      document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
  },

  onDeviceReady: function () {
      this.receivedEvent('deviceready');
      var phone = "smartphone"
      var r = "_"
      var ok = "ok"
      var err = "err"

      var username = "user.name"
      var points = 27000
      var ranking = 14

      var logged = "notlogged"
      var km = null
      var mode = "null"

      //Buttons
      document.getElementById("btn_login").addEventListener("click", function () { logged="ok" }, false);
      document.getElementById("btn_logout").addEventListener("click", function () { logged="notlogged" }, false);
      document.getElementById("btn_syncok").addEventListener("click", function () { send(phone+r+"sync"+r+ok+r+ok); }, false);
      document.getElementById("btn_syncerr").addEventListener("click", function () { send(phone+r+"sync"+r+err+r+"Failed to synchronize :("); }, false);

      //Receiver
      AndroidWearApi.getMessages(function (data) {
        var sender = data.split("_")[0]
        var activity = data.split("_")[1]
        var value = data.split("_")[2]
        if(sender = "wearable"){
          switch (activity) {
            case "logstatus": send(phone+r+activity+r+logged+r+logged); break;
            case "starttracking": km = 0; mode = value; send(phone+r+activity+r+mode+r+ok); break;
            case "stoptracking": send(phone+r+activity+r+mode+r+ok); break;
            case "getdistance": km++; send(phone+r+activity+r+mode+r+km); break;
            case "getinfo":
              switch (value) {
                case "username": send(phone+r+activity+r+value+r+username); break;
                case "points": send(phone+r+activity+r+value+r+points); break;
                case "ranking": send(phone+r+activity+r+yvalue+r+ranking); break;
                default: send(phone+r+activity+r+mode+r+err);
              } break;
            default: send(phone+r+err+r+err+r+err);
          }
        }
          // alert(data);
        document.getElementById("txt_logstatus").innerHTML = "Logged: " + logged;
        document.getElementById("txt_mode").innerHTML = "Mode: " + mode;
        document.getElementById("txt_km").innerHTML = "Km: " + km;
      });
  },

  receivedEvent: function (id) {
      var parentElement = document.getElementById(id);
      var listeningElement = parentElement.querySelector('.listening');
      var receivedElement = parentElement.querySelector('.received');

      listeningElement.setAttribute('style', 'display:none;');
      receivedElement.setAttribute('style', 'display:block;');
  },
};
app.initialize();

function send(msg) {
  AndroidWearApi.sendMessage(msg, function (data) {
      Console.log(data);
  });
}
