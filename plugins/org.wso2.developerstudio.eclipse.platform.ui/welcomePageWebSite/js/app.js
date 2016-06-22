var svgArea = Snap("#svgArea");

var colorOrange = "#FF9900";
var colorGrey1 = "#333333";
var colorGrey2 = "#666666";
var colorGrey3 = "#999999";
var colorGrey4 = "#BBBBBB";


function setViewPortFullScreen() {
    svgArea.animate({viewBox: '0 0 1000 1000'}, 400);
}


var selectedNode = null;

var setSelectedNode= function(node){
    if(selectedNode != null && (selectedNode != node || node == null)){
        selectedNode.nodes.forEach(function(child){
            child.circle.animate({r:0}, 400, null, function(){
                child.circle.remove();
            });

            child.line.animate({x2:child.line.attr('x1'), y2:child.line.attr('y1')}, 200, null, function(){
                child.line.remove();
            });

            child.text.remove();

        });
        selectedNode.circle.animate({r: parseInt(selectedNode.circle.attr('r')) - 20}, 200);
    }else if(selectedNode == node){
        return;
    }
    if(node != null)
    {
        node.circle.animate({r: parseInt(node.circle.attr('r')) + 20}, 200);
    }
    selectedNode = node;
};

var welcomeNodeArray = [
    {title: "DevStudio Kernel", nodes: [
        {title: "node1", icon: "", link: ""},
        {title: "node1", icon: "", link: ""}
    ]},
    {title: "ESB Tools", nodes: []},
    {title: "AppFactory Tools", nodes: [
        {title: "node1", icon: "", link: ""},
        {title: "node1", icon: "", link: ""}
    ]},
    {title: "Governance Registry Tools", nodes: []},
    {title: "Test", nodes: []}
];

function toRadians(angle) {
    return angle * (Math.PI / 180);
}

var angleOffset = toRadians(20);
var anglePerMainItem = toRadians(360) / (welcomeNodeArray.length);


var cy = $('#pageRow').height() / 2;
var cx = $('#pageRow').width() / 2;
var cr = 50;


var addCenterText = function () {
    svgArea.text(cx, cy, "WSO2 Developer Studio")
        .attr({"text-anchor": "middle", "font-size":"15px"})
        .addClass('.title');
};

var addCenteredText = function (x, y, text) {
    return svgArea.text(x, y, text)
        .attr({"text-anchor": "middle", "font-size":"15px"})
        .addClass('.title');
}

var centerCircle = svgArea.circle(cx, cy, cr);

var centerCircleOnHover = function (event, t) {
    cr = 110;
    centerCircle.animate({r: cr, strokeWidth: 15}, 100);
}

var centerCircleHoverOff = function (event, t) {
    cr = 100;
    centerCircle.animate({r: cr, strokeWidth: 10}, 100);
}

centerCircle.attr({
    fill: colorOrange,
    stroke: colorGrey1,
    strokeWidth: 10
}).addClass('centerCircle')
    .animate({r: 100}, 1000, null, addCenterText)
    .hover(centerCircleOnHover, centerCircleHoverOff, centerCircle, centerCircle);

var getEndpointForPath = function (angle, lineLength) {
    var point = {};
    point.x = cx + (lineLength * Math.sin(angle));
    point.y = cy - (lineLength * Math.cos(angle));
    return point;
}

var getEndpointForChildPath = function (parent, angle, lineLength) {
    var point = {};
    point.x = parseInt(parent.attr('cx')) + (lineLength * Math.sin(angle));
    point.y = parseInt(parent.attr('cy'))  - (lineLength * Math.cos(angle));
    return point;
}

var count = 1;
function setViewPortToChild(childCircle) {
    svgArea.attr({viewBox: (parseInt(childCircle.attr('cx')) - 300) + ' ' + (parseInt(childCircle.attr('cy')) - 300) + ' 600 600'});
}
welcomeNodeArray.forEach(function (welcomeNode) {
    var line1Endpoint = getEndpointForPath(angleOffset + count * anglePerMainItem, cr);
    var line2Endpoint = getEndpointForPath(angleOffset + count * anglePerMainItem, 300);
    var line = svgArea.line(cx, cy, line1Endpoint.x, line1Endpoint.y)
        .insertBefore(centerCircle)
        .attr({strokeWidth: 10, stroke: colorGrey1, strokeLinecap: "round", opacity:100})
        .animate({x1: cx, y1: cy, x2: line2Endpoint.x, y2: line2Endpoint.y }, 1000);
    var circle = svgArea.circle(line1Endpoint.x, line1Endpoint.y, cr)
        .attr({fill: "#bada55", filter:"url('#goo')"})
        .drag(function () {
            this.data("dataNode").message = "hit";
        })
        .animate({ cx: line2Endpoint.x, cy: line2Endpoint.y}, 1000, null, function () {
            addCenteredText(line2Endpoint.x, line2Endpoint.y, welcomeNode.title);
        })
        .addClass("childCircle");
    line.data("dataNode", welcomeNode);
    circle.data("dataNode", welcomeNode);
    welcomeNode.point = line2Endpoint;
    welcomeNode.line = line;
    welcomeNode.circle = circle;

    var cloneNodeForItem = $('#templateChildMenu').clone();
    cloneNodeForItem.attr("id", 'childNode' + count);
    cloneNodeForItem.css("left",line2Endpoint.x );
    cloneNodeForItem.css("top",line2Endpoint.y );
    cloneNodeForItem.css("display","block");
    $('body').append(cloneNodeForItem);

    circle.click(function(){
        if(selectedNode == welcomeNode){
            return;
        }
        setSelectedNode(welcomeNode);
        var anglePerChild = toRadians(360) / welcomeNode.nodes.length;
        var childCount = 1;
        var childAngleOffset = toRadians(60);
        welcomeNode.nodes.forEach(function (childNode) {
            var childEP1 = getEndpointForChildPath(circle, childAngleOffset + childCount * anglePerChild, 100);
            var childEP2 = getEndpointForChildPath(circle, childAngleOffset + childCount * anglePerChild, 200);
            var childLine = svgArea.line(parseInt(circle.attr('cx')), parseInt(circle.attr('cy')), childEP1.x, childEP1.y)
                .insertBefore(circle)
                .attr({strokeWidth: 5, stroke: colorGrey1, strokeLinecap: "round"})
                .animate({x1: parseInt(circle.attr('cx')), y1:  parseInt(circle.attr('cy')), x2: childEP2.x, y2: childEP2.y }, 400);
            var childCircle = svgArea.circle(childEP1.x, childEP1.y, 50)
                                .animate({ cx: childEP2.x, cy: childEP2.y}, 400, null, function () {
                                    childNode.text = addCenteredText(childEP2.x, childEP2.y, childNode.title);
                                });
            childNode.line = childLine;
            childNode.circle = childCircle;
            childNode.ep2 = childEP2;
            childLine.data("dataNode", welcomeNode);
            childCircle.data("dataNode", welcomeNode);
            childCount++
        });
        line.animate({opacity:0.1,strokeWidth: 5}, 400);
        setViewPortToChild(circle);
        //svgArea.attr({viewBox:'0 2000 1000 1000'}, 200);
    });

    circle.click(function(){
        if(selectedNode == welcomeNode){
            return;
        }
        setSelectedNode(welcomeNode);
        var anglePerChild = toRadians(360) / welcomeNode.nodes.length;
        var childCount = 1;
        var childAngleOffset = toRadians(60);

        line.animate({opacity:0.1,strokeWidth: 5}, 400);
        //setViewPortToChild(circle);
        svgArea.attr({viewBox:'0 2000 1000 1000'}, 200);
    });

    circle.hover(function () {

    }, function () {

    });
    count++;

});

$('#zoomInIconP').click(function(){
    setViewPortFullScreen();
    selectedNode.line.animate({opacity:100, strokeWidth: 10}, 400);
    setSelectedNode(null);
});

