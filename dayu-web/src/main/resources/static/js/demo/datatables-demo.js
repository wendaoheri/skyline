$(document).ready(function () {
  var tableCols = [
    {"data": "id"},
    {"data": "user"},
    {"data": "name"},
    {"data": "queue"},
    {"data": "state"},
    {"data": "finalStatus"},
    {"data": "progress"},
    {"data": "trackingUI"},
    {"data": "trackingUrl"},
    {"data": "diagnostics"},
    {"data": "clusterId"},
    {"data": "applicationType"},
    {"data": "applicationTags"},
    {"data": "startedTime"},
    {"data": "finishedTime"},
    {"data": "elapsedTime"},
    {"data": "amContainerLogs"},
    {"data": "amHostHttpAddress"},
    {"data": "allocatedMB"},
    {"data": "allocatedVCores"},
    {"data": "runningContainers"},
    {"data": "memorySeconds"},
    {"data": "vcoreSeconds"},
    {"data": "preemptedResourceMB"},
    {"data": "preemptedResourceVCores"},
    {"data": "numNonAMContainerPreempted"},
    {"data": "numAMContainerPreempted"},
    {"data": "logAggregationStatus"},
    {"data": "scheduleId"}
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
        {
          "filterType": "EQ",
          "name": "queue",
          "value": "root.dsp"
        },
        {
          "filterType": "GT",
          "name": "startedTime",
          "value": "1551863321234"
        },
        {
          "filterType": "LT",
          "name": "startedTime",
          "value": "1551863722370"
        }
      ],
      "keyword": "hive",
      "page": 0,
      "size": 10,
      "orders": [
        {
          "name": "elapsedTime",
          "orderType": "DESC"
        },
        {
          "name": "finishedTime",
          "orderType": "DESC"
        }
      ]
    }
    $.ajax({
      "url": "applications/search",
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
});