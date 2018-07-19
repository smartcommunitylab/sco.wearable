var app = {
  initialize: function () {
      document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
  },

  onDeviceReady: function () {
      this.receivedEvent('deviceready');

      document.getElementById("btn_login").addEventListener("click", function () { send('smartphone_logstatus_ok'); }, false);

      AndroidWearApi.getMessages(function (data) {
          alert(data);
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
