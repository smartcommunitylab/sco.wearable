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

      var logged = "notlogged"
      var km = null
      var mode = "null"

      //Buttons
      document.getElementById("btn_login").addEventListener("click", function () { logged="ok" }, false);
      document.getElementById("btn_logout").addEventListener("click", function () { logged="notlogged" }, false);

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
            default: send(phone+r+err+r+err+r+err); break;
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
