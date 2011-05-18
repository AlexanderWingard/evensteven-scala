var chart1; // globally available
$(document).ready(function() {
      chart1 = new Highcharts.Chart({
         chart: {
            renderTo: 'evenchart',
            defaultSeriesType: 'bar'
         },
         title: {
            text: 'EvenSteven'
         },
         xAxis: {
         },
         yAxis: {

         },


      });
   });

function callSendEven() {
    sendEven("sendEvenCallback", $("#input").val())
}

function sendEvenCallback(result) {
    alert(result);
}