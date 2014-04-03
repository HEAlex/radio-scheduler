var files, calendar, wrapper, days, player, uploader_container;

jQuery.fn.swap = function (b) {
    b = jQuery(b)[0];
    var a = this[0];

    var t = a.parentNode.insertBefore(document.createTextNode(''), a);
    b.parentNode.insertBefore(a, b);
    t.parentNode.insertBefore(b, t);
    t.parentNode.removeChild(t);

    return this;
};

function onResize() {
    var dw = Math.floor(parseInt($(calendar).css('width')) / 7);
    $(days).css('width', dw);
}

function parseDate(date) {
    var t = date.split('.'),
        d = new Date(2000 + parseInt(t[2]), parseInt(t[1]) - 1, t[0]);
    return d;
}

var oneM = 1000000;

function microSecTime(t) {
    return {
        s:Math.round((t / oneM) % 60),
        m:Math.round((t / (oneM * 60)))
    }
}

function formatTime(time) {
    var min = time.m,
        sec = time.s;
    if (('' + min).length < 2) min = '0' + min;
    if (('' + sec).length < 2) sec = '0' + sec;
    return min + ":" + sec;
}

function showDuration(elem) {
    var d = $(elem).attr('duration');
    if (typeof d != 'undefined') {
        var time = microSecTime(d);
        $('<span class="time"></span>').html(formatTime(time)).appendTo(elem);
    }
}

function updateTotalTime(elem) {
    var total = 0,
        kids = $(elem).children('li');
    var c = $(elem).parent().children('h6').children('.totalTime');
    $(kids).each(function () {
        total += $(this).attr('duration') - 0;
    });
    var time = microSecTime(total);
    c.text(formatTime(time));
}

function addControls(elem) {
    var controls = $('<span class="controls"></span>').appendTo(elem),
        up = $('<span class="icon-chevron-up"></span>').appendTo(controls),
        down = $('<span class="icon-chevron-down"></span>').appendTo(controls),
        remove = $('<span class="icon-remove"></span>').appendTo(controls);

    $(up).click(function () {
        moveTrackUp(elem);
    });
    $(down).click(function () {
        moveTrackDown(elem);
    });
    $(remove).click(function () {
        removeTrack(elem);
    });
}

function updateControls(elem) {
    $(elem).find('.button.up').click(function () {
        moveTrackUp(elem);
    });
    $(elem).find('.button.down').click(function () {
        moveTrackDown(elem);
    });
    $(elem).find('.button.remove').click(function () {
        removeTrack(elem);
    });
}

function isFirstItem(elem) {
    return $(elem).index() == 0;
}

function isLastItem(elem) {
    var length = $(elem).parent().children().size();
    return $(elem).index() == length - 1;
}

function addTrack(elem, date, timePart, pos, fileId) {
    $.ajax("/api/tracks", {
        type:"post",
        data:{
            date:date,
            timePart:timePart,
            fileId:fileId,
            position:pos
        },
        success:function (res) {
            if (res.success) {
                $(elem).attr('trackID', res.track.id);
                addControls(elem);
                showDuration(elem);
            } else {
                $(elem).remove();
            }
        }
    });
}

function moveTrackUp(track) {
    if (!isFirstItem(track)) {
        var index = $(track).index(),
            trackId = $(track).attr('trackId');
        $.get("/tracks/moveUP/" + trackId, function (res) {
            if (res.success) {
                track = $(track).swap($(track).siblings().get(index - 1));
            }
        });
    }
}

function moveTrackDown(track) {
    if (!isLastItem(track)) {
        var index = $(track).index(),
            trackId = $(track).attr('trackId');
        $.get("/tracks/moveDOWN/" + trackId, function (res) {
            if (res.success) {
                track = $(track).swap($(track).siblings().get(index));
            }
        });
    }
}

function removeTrack(track) {
    var trackId = $(track).attr('trackId');
    $.ajax("/api/tracks/" + trackId, {
        type:'DELETE',
        success:function (res) {
            if (res.success) {
                var parent = $(track).parent();
                $(track).remove();
                updateTotalTime(parent);
            }
        }
    });
}

function deleteFile(elem) {
    var fileId = $(elem).attr('fileId'),
        filename = $(elem).children('.filename').first().text();
    if (confirm("Вы точно хотите удалить файл\n" + filename)) {
        $.ajax(encodeURI("/api/files/" + fileId), {
            type:'DELETE',
            success:function (res) {
                if (res.success) {
                    var selector = '[fileid=' + fileId + ']';
                    $('#files div' + selector).remove();
                    $('#calendar li' + selector).each(function () {
                        var parent = $(this).parent();
                        $(this).remove();
                        updateTotalTime(parent);
                    });
                }
            }
        });
    }
}

function isPlaying(elem) {
    return $(elem).hasClass('playing');
}

function setPlaying(elem) {
    $(elem).addClass('playing');
}

function unsetPlaying(elem) {
    $(elem).removeClass('playing');
}

function playCurrent(elem) {
    if (isPlaying(player.lastElem)) {
        unsetPlaying(player.lastElem);
    }
    if (elem != player.lastElem && !isPlaying(elem)) {
        setPlaying(elem);
        player.jPlayer("setMedia", {
            mp3:encodeURI('/api/tracks/play/' + $(elem).attr('fileId'))
        }).jPlayer('play');
        player.lastElem = elem;
    } else {
        unsetPlaying(elem);
        player.jPlayer('stop');
        player.jPlayer("clearMedia");
        player.lastElem = null;
    }
}

function getWeek(date) {
    if (date instanceof Date) {
        $.get("/api/week", {
            date: '' + date.getDate() + '.' + (date.getMonth() + 1) + '.' + ('' + date.getFullYear()).substring(2)
        }, function (res) {
            days = $('#days').replaceWith(res);
            updateDatePanel();
            onResize();
            initDays();
        });
    } else {
        throw "Date parameter must be Date type.";
    }
}

function initDragable(elems) {
    $(elems).draggable({
        appendTo:'body',
        helper:function (ui) {
            var t = $(ui.currentTarget),
                c = $('<div class="file"></div>')
                    .attr({
                        'fileid':$(t).attr('fileid'),
                        'duration':$(t).attr('duration')
                    })
                    .text($(t).text())
                    .prepend('<ins class="file-icon"></ins>')
            return c;
        }
    });
}

function initDays() {
    $(calendar).find('td').droppable({
        //activeClass:"ui-state-default",
        hoverClass:'drop-hover',
        drop:function (event, ui) {
            var trackList = $(this).children('ul')[0],
                fileId = $(ui.draggable).attr('fileId'),
                duration = $(ui.draggable).attr('duration'),
                track = $('<li></li>')
                    .text($(ui.draggable).text().trim())
                    .addClass("ui-state-default ui-corner-all")
                    .attr('fileId', fileId)
                    .attr('duration', duration)
                    .appendTo(trackList);

            updateTotalTime(trackList);

            addTrack(track,
                $(this).attr('date'),
                $(this).attr('timePart'),
                $(this).find('ul li').size(),
                fileId);
        }
    });

    $(calendar).find('li').each(function () {
        addControls(this);
        showDuration(this);
    });

    $(calendar).find('ul').each(function () {
        updateTotalTime(this);
    });
}

function updateDatePanel(){
    var d = $('#days'),
        startDate = $(d).attr('startDate'),
        endDate = $(d).attr('endDate');
    $('#datePanel').html(startDate + ' — ' + endDate);
}

$(document).ready(function () {
    files = $('#files');
    calendar = $('#calendar');
    wrapper = $('#wrapper');
    days = $(calendar).find('td');

    $(window).resize(onResize);

    $(files).jstree({
        "core":{ "initially_open":[ "root" ] },
        "themes":{
            "theme":"farm"
        },
        "json_data":{
            "ajax":{
                "url":"/api/files",
                "data":function (n) {
                    return { id:n.attr ? n.attr("id") : 0 };
                }
            }
        },
        "plugins":[ "themes", "json_data" ]
    }).bind("loaded.jstree", function (event, data) {
            var ff = $(files).find('li.jstree-leaf[fileid]');
            initDragable(ff);
            $(ff).find('a ins.jstree-icon').click(function () {
                playCurrent($(this).parent().parent()[0]);
            });
        });

    initDays();

    onResize();

    $(files).children('.file').each(function () {
        showDuration(this);
    });

    player = $("<div></div>").jPlayer({
        swfPath:"/js",
        solution:"flash",
        supplied:"mp3"
    }).appendTo(document.body);

    $('#prevWeek').click(function () {
        var date = parseDate($('#days').attr('startDate'));
        date.setDate(date.getDate() - 1);
        getWeek(date);
    });

    $('#nextWeek').click(function () {
        var date = parseDate($('#days').attr('endDate'));
        date.setDate(date.getDate() + 1);
        getWeek(date);
    });

    $('#curWeek').click(function() {
        var date = new Date();
        getWeek(date);
    });

    $('#refreshButton').click(function(){
        $.getJSON("/api/resync", function(res){
            if (!res.success) {
                alert("Произошла ошибка. \nВ случае повторного возникновения\n свяжитесь с администратором");
            }
            window.location.reload();
        });
    });
})
;

/* function to fix the -10000 pixel limit of jquery.animate */
$.fx.prototype.cur = function () {
    if (this.elem[this.prop] != null && (!this.elem.style || this.elem.style[this.prop] == null)) {
        return this.elem[ this.prop ];
    }
    var r = parseFloat(jQuery.css(this.elem, this.prop));
    return typeof r == 'undefined' ? 0 : r;
}

/* function to load new content dynamically */
function LoadNewContent(id, file) {
    $("#" + id + " .customScrollBox .content").load(file, function () {
        mCustomScrollbars();
    });
}

function showUploader() {

    $('#fileuploader-modal').modal();

    var uploader = new plupload.Uploader({
        runtimes:'gears,html5,flash,silverlight,browserplus',
        browse_button:'pickfiles',
        container:'uploader-container',
        max_file_size:'100mb',
        url:'/files/upload',
        flash_swf_url:'/js/plupload/plupload.flash.swf',
        silverlight_xap_url:'/js/plupload/plupload.silverlight.xap',
        filters:[
            {title:"MP3 audio files", extensions:"mp3"}
        ],
        resize:{width:320, height:240, quality:90}
    });
    $('#filelist').empty();
    /*uploader.bind('Init', function (up, params) {
     $('#filelist').html("<div>Current runtime: " + params.runtime + "</div>");
     });*/

    $('#uploadfiles').click(function (e) {
        uploader.start();
        e.preventDefault();
    });

    uploader.init();

    uploader.bind('FilesAdded', function (up, files) {
        $.each(files, function (i, file) {
            $('#filelist').append(
                '<div id="' + file.id + '">' +
                    file.name + ' (' + plupload.formatSize(file.size) + ') <b></b>' +
                    '</div>');
        });

        up.refresh(); // Reposition Flash/Silverlight
    });

    uploader.bind('UploadProgress', function (up, file) {
        $('#' + file.id + " b").html(file.percent + "%");
    });

    uploader.bind('Error', function (up, err) {
        $('#filelist').append("<div>Error: " + err.code +
            ", Message: " + err.message +
            (err.file ? ", File: " + err.file.name : "") +
            "</div>"
        );

        up.refresh(); // Reposition Flash/Silverlight
    });

    uploader.bind('FileUploaded', function (up, file) {
        $('#' + file.id + " b").html("100%");
        updateFileList();
    });
}
