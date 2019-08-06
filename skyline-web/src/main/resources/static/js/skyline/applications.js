(function($) {
  "use strict"; // Start of use strict

  var tableCols = [
    {"data": "_id"},
    {"data": "user"},
    {"data": "name"},
    {"data": "queue"},
    {"data": "state"},
    {"data": "final_status"},
    {"data": "application_type"},
    {"data": "started_time"},
    {"data": "finished_time"},
    {"data": "elapsed_time"},
    {"data": "memory_seconds"},
    {"data": "vcore_seconds"}
  ];
  $('#dataTable').DataTable({
    "processing": true,
    "serverSide": true,
    "search": false,
    "filter": false,
    "ajax": retrieveData,
    "columns": tableCols,
    "buttons": [
      'copy', 'excel', 'pdf'
    ]
  });

  function retrieveData(data, callback, settings) {
    var req = {
      "filters": [

      ],
      "keyword": "application*",
      "fields":["application_id","user","name","queue","state","final_status","application_type","started_time","finished_time","elapsed_time","memory_seconds","vcore_seconds"],
      "page":1,
      "size":10,
      "orders": [
        {
          "name": "elapsed_time",
          "orderType": "DESC"
        },
        {
          "name": "finished_time",
          "orderType": "DESC"
        }
      ]
    }
    $.ajax({
      "url": "ws/v1/applications/search",
      "method": "POST",
      "contentType": "application/json;charset=UTF-8",
      "data": JSON.stringify(req)
    }).done(function (msg) {
      msg.draw = data.draw;
      msg.recordsTotal = msg.totalElements;
      msg.recordsFiltered = msg.totalElements;
      callback(msg)
    });
  }

})(jQuery); // End of use strict
