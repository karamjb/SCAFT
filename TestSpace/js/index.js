(function() {
  var app = angular.module('chatApp', []);

    //Define Routing for app
//Uri /AddNewOrder -> template add_order.html and Controller AddOrderController
//Uri /ShowOrders -> template show_orders.html and Controller AddOrderController


  app.controller('MessageCtrl', function($scope) {
    $scope.messages = [{
      Name: 'George Clooney',
      Message: "The only failure is not to try"
    }, {
      Name: 'Seth Rogen',
      Message: "I grew up in Vancouver, man. That's where more than half of my style comes from."
    }/*, {
      Name: 'John Lydon',
      Message: "There's nothing glorious in dying. Anyone can do it."
    }*/];

    $scope.users=[{
        Name:'Karam Jabareen',
        Status:'active'
    }, {
        Name:'Ali Abed',
        Status:'offline'
    }, {
        Name:'Muhammad Farok',
        Status:'offline'
    }, {
        Name:'Adam Shagash',
        Status:'active'
    }];

    $scope.sendMessage = function (keyEvent) {
        if (keyEvent.which === 13){
            //var input = angular.element( document.querySelector( 'chatbox_input' ) );
            var input = $scope.c;
            if(input.length!=0) {
                var ms = {
                    Name: "me",
                    Message: input
                };
                $scope.messages.push(ms);
                $scope.c = "";
            }
        }
    }
  });

})();
