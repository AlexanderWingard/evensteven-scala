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