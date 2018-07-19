var app = {
  initialize: function () {
      document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
  },

  onDeviceReady: function () {
      this.receivedEvent('deviceready');
      var phone = "smartphone"
      var r = "_"
      var ok = "ok"
      var logged = "notlogged"

      //Buttons
      document.getElementById("btn_login").addEventListener("click", function () { logged="ok" }, false);
      document.getElementById("btn_logout").addEventListener("click", function () { logged="notlogged" }, false);

      //Receiver
      AndroidWearApi.getMessages(function (data) {
        var sender = data.split("_")[0]
        var activity = data.split("_")[1]
        var status = data.split("_")[2]
        if(sender = "wearable"){
          switch (activity) {
            case "logstatus": send(phone+r+activity+r+logged+r+logged); break;
          }
        }
          // alert(data);
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
