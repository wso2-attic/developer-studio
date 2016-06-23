var svgArea = Snap("#svgArea");

var colorOrange = "#FF7F00";//"#FF9900";
var colorGrey1 = "#333333";
var colorGrey2 = "#666666";
var colorGrey3 = "#999999";
var colorGrey4 = "#BBBBBB";

var w = window,
    d = document,
    e = d.documentElement,
    g = d.getElementsByTagName('body')[0],
    x = w.innerWidth || e.clientWidth || g.clientWidth,
    totalHeight = w.innerHeight || e.clientHeight || g.clientHeight;


var animationDuration = 1000;

function setViewPortFullScreen() {
    svgArea.animate({viewBox: '0 0 1000 1000'}, 400);
}


var selectedNode = null;

var setSelectedNode = function (node) {
    if (selectedNode != null && (selectedNode != node || node == null)) {
        selectedNode.circle.attr({strokeWidth: 0});
        selectedNode.nodes.forEach(function (child) {
            child.circle.animate({r: 0}, 400, null, function () {
                child.circle.remove();
            });

            child.line.animate({x2: child.line.attr('x1'), y2: child.line.attr('y1')}, 200, null, function () {
                child.line.remove();
            });

            child.text.remove();

        });
        selectedNode.circle.animate({r: parseInt(selectedNode.circle.attr('r')) - 20}, 200);
    } else if (selectedNode == node) {
        return;
    }
    if (node != null) {
        node.circle.animate({r: parseInt(node.circle.attr('r')) + 20}, 200);
        node.circle.attr({strokeWidth: 10});
    }
    selectedNode = node;
};

function loadWelcomeNodes() {
   var contributionsString = GetIDEDashboardWizards();
   var contributions = JSON.parse(contributionsString);
   var welcomeNodes = [];

   contributions.forEach(function(contribution){
       var welcomeNode = {};
       welcomeNode.title = contribution.name;
       welcomeNode.icon = contribution.iconURL;
       welcomeNode.nodes = [];
       contribution.wizards.forEach(function(wizard){
           var wizardNode = {};
           wizardNode.title = wizard.id;
           wizardNode.wizardID = wizard.name;
           wizardNode.priority = wizard.priority;
           welcomeNode.nodes.push(wizardNode);
       });
       welcomeNodes.push(welcomeNode);
   });
   return welcomeNodes;
}
//var welcomeNodeArray = [
//    {title: "Kernel", nodes: [
//        {title: "node1", icon: "", link: ""},
//        {title: "node1", icon: "", link: ""}
//    ]},
//    {title: "ESB", nodes: []},
//    {title: "AppFactory", nodes: [
//        {title: "node1", icon: "", link: ""},
//        {title: "node1", icon: "", link: ""}
//    ]},
//    {title: "GReg", nodes: []},
//    {title: "Test", nodes: []}
//];

var welcomeNodeArray = loadWelcomeNodes();

function toRadians(angle) {
    return angle * (Math.PI / 180);
}

var angleOffset = toRadians(20);
var anglePerMainItem = toRadians(360) / (welcomeNodeArray.length);


var cy = $('#pageRow').height() / 2;
var cx = $('#pageRow').width() / 2;
var cr = 50;

var centeredMainText;
var addCenteredMainText = function () {
    centeredMainText = svgArea.text(cx, cy, "WSO2 Developer Studio")
        .attr({"text-anchor": "middle", "font-size": "15px"})
        .addClass('.title');
};


var addCenteredText = function (x, y, text, offset) {
    if(offset > 0){
        return svgArea.text(x, y, text)
            .attr({"text-anchor": "middle", "font-size": "15px", filter: "", opacity: 0, dy: offset})
            .addClass('.title');
    }
    return svgArea.text(x, y, text)
        .attr({"text-anchor": "middle", "font-size": "15px", filter: "", opacity: 0})
        .addClass('.title');
};

var centerCircle = svgArea.circle(cx, cy, cr);

var centerCircleOnHover = function (event, t) {
    cr = 110;
    centerCircle.animate({r: cr, strokeWidth: 15}, 100);
};

var centerCircleHoverOff = function (event, t) {
    cr = 100;
    centerCircle.animate({r: cr, strokeWidth: 10}, 100);
};

centerCircle.attr({
    fill: colorOrange,
    stroke: colorGrey1,
    strokeWidth: 10
}).addClass('centerCircle')
    .animate({r: 100}, 1000, null, addCenteredMainText)
    .hover(centerCircleOnHover, centerCircleHoverOff, centerCircle, centerCircle);

var getEndpointForPath = function (angle, lineLength) {
    var point = {};
    point.x = cx + (lineLength * Math.sin(angle));
    point.y = cy - (lineLength * Math.cos(angle));
    return point;
};

var getEndpointForChildPath = function (parent, angle, lineLength) {
    var point = {};
    point.x = parseInt(parent.attr('cx')) + (lineLength * Math.sin(angle));
    point.y = parseInt(parent.attr('cy')) - (lineLength * Math.cos(angle));
    return point;
};

var count = 1;
function setViewPortToChild(childCircle) {
    svgArea.animate({viewBox: (parseInt(childCircle.attr('cx')) - 300) + ' ' + (parseInt(childCircle.attr('cy')) - 300) + ' 600 600'}, 1000);
}
function setViewPortToChild(childCircle, radius) {
    svgArea.animate({viewBox: (parseInt(childCircle.attr('cx')) - radius/2) + ' ' + (parseInt(childCircle.attr('cy')) - radius/2) + ' ' + radius + ' ' + radius}, 1000);
}

function addImageForChild(childNode){
    var imageData = GetWizardIconData(childNode.wizardID);
    var imageSrc = "data:image/png;base64," + imageData;
    svgArea.image(imageSrc, childNode.ep2.x, childNode.ep2.y, 15,15);
}


function getRandomArbitrary(min, max) {
    return Math.random() * (max - min) + min;
}
setViewPortFullScreen();

welcomeNodeArray.forEach(function (welcomeNode) {
    var line1Endpoint = getEndpointForPath(angleOffset + count * anglePerMainItem, cr);
    var line2Endpoint = getEndpointForPath(angleOffset + count * anglePerMainItem, 250);
    var line = svgArea.line(cx, cy, line1Endpoint.x, line1Endpoint.y)
        .insertBefore(centerCircle)
        .attr({strokeWidth: 10, stroke: colorGrey1, strokeLinecap: "round", opacity: 100, filter: ""})
        .animate({x1: cx, y1: cy, x2: line2Endpoint.x, y2: line2Endpoint.y }, 1000);
    welcomeNode.text = addCenteredText(line2Endpoint.x, line2Endpoint.y, welcomeNode.title);
    var circle = svgArea.circle(line1Endpoint.x, line1Endpoint.y, cr)
        .insertBefore(welcomeNode.text)
        .attr({fill: colorOrange, stroke: colorGrey1, strokeWidth: 0})
        .hover(function () {
            if(selectedNode != welcomeNode){
                circle.animate({strokeWidth: 10}, 200);
            }
        }, function () {
            if(selectedNode != welcomeNode){
                circle.animate({strokeWidth: 0}, 200);
            }
        })
        .animate({ cx: line2Endpoint.x, cy: line2Endpoint.y}, 1000, null, function () {
            welcomeNode.text.animate({opacity: 100}, 400);
        })
        .addClass("childCircle");
    line.data("dataNode", welcomeNode);
    circle.data("dataNode", welcomeNode);
    welcomeNode.point = line2Endpoint;
    welcomeNode.line = line;
    welcomeNode.circle = circle;


    circle.click(function () {
        if (selectedNode == welcomeNode) {
            setSelectedNode(null);
            setViewPortFullScreen();
            showUnselectedNodes();
            return;
        }
        setSelectedNode(welcomeNode);
        hideUnselectedNodes();
        setViewPortToChild(circle, 600);
        setTimeout(function () {
            var anglePerChild = toRadians(360) / welcomeNode.nodes.length;
            var childCount = 1;
            if(welcomeNode.nodes.length > 8){
                setViewPortToChild(circle, 1100);
            }
            var childAngleOffset = getRandomArbitrary(toRadians(360),toRadians(20));
            welcomeNode.nodes.forEach(function (childNode) {
                var childEP1 = getEndpointForChildPath(circle, childAngleOffset + childCount * anglePerChild, 100);
                var childEP2 = getEndpointForChildPath(circle, childAngleOffset + childCount * anglePerChild, 200);
                if(welcomeNode.nodes.length > 8){
                    childEP2 = getEndpointForChildPath(circle, childAngleOffset + childCount * anglePerChild, getRandomArbitrary(275,500));

                }
                var childLine = svgArea.line(parseInt(circle.attr('cx')), parseInt(circle.attr('cy')), childEP1.x, childEP1.y)
                    .insertBefore(circle)
                    .attr({strokeWidth: 5, stroke: colorGrey1, strokeLinecap: "round"})
                    .animate({x1: parseInt(circle.attr('cx')), y1: parseInt(circle.attr('cy')), x2: childEP2.x, y2: childEP2.y }, 400);

                childNode.text = addCenteredText(childEP2.x, childEP2.y, GetWizardDescription(childNode.wizardID));
                var childCircle = svgArea.circle(childEP1.x, childEP1.y, 20)
                    .insertBefore(childNode.text)
                    .attr({fill: colorOrange, stroke: colorGrey1, strokeWidth: 0})
                    .animate({ cx: childEP2.x, cy: childEP2.y}, 400, null, function () {
                        childNode.text.animate({opacity: 100}, 400);
                    })
                    .hover(function () {
                        childCircle.animate({strokeWidth: 5}, 200);
                    }, function () {
                        childCircle.animate({strokeWidth: 0}, 200);
                    })
                    .addClass("leafCircle")
                    .click(function(){OpenIDEWizard(childNode.wizardID);});
                childNode.line = childLine;
                childNode.circle = childCircle;
                childNode.ep2 = childEP2;
                childLine.data("dataNode", welcomeNode);
                childCircle.data("dataNode", welcomeNode);

                addImageForChild(childNode);
                childCount++
            });

        }, animationDuration + 250);
    });

    circle.hover(function () {

    }, function () {

    });
    count++;

});


function hideUnselectedNodes() {
    if (selectedNode != null) {
        centerCircle.animate({opacity: 0}, animationDuration);
        centeredMainText.animate({opacity: 0}, animationDuration);
        welcomeNodeArray.forEach(function (node) {
            node.line.animate({opacity: 0}, animationDuration / 5);
            if (node != selectedNode) {
                node.circle.animate({opacity: 0}, animationDuration);
                node.text.animate({opacity: 0}, animationDuration);
            }
        });
    }
}

function showUnselectedNodes() {
    centerCircle.animate({opacity: 100}, animationDuration);
    centeredMainText.animate({opacity: 100}, animationDuration);
    welcomeNodeArray.forEach(function (node) {
        node.line.animate({opacity: 100}, animationDuration);
        if (node != selectedNode) {
            node.circle.animate({opacity: 100}, animationDuration);
            node.text.animate({opacity: 100}, animationDuration);
        }
    });
}

$('#zoomInIconP').click(function () {
    setSelectedNode(null);
    setTimeout(function () {
        setViewPortFullScreen();
        showUnselectedNodes();
    }, animationDuration);
});

