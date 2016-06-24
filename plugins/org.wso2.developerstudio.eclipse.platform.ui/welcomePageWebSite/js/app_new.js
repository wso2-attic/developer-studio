var svgArea = Snap("#svgArea");

var colorOrange = "#f47a20";//"#";
var colorOrange2 = "#ffffff";
var colorBorder = "#ffffff";
var colorGrey1 = "#333333";
var colorGrey2 = "#666666";
var colorGrey3 = "#999999";
var colorGrey4 = "#BBBBBB";
var rectAnimationDuration = 50;

var centerLogo;

var w = window,
    d = document,
    e = d.documentElement,
    g = d.getElementsByTagName('body')[0],
    x = w.innerWidth || e.clientWidth || g.clientWidth,
    totalHeight = w.innerHeight || e.clientHeight || g.clientHeight;

var animationDuration = 250;

//svgArea.attr({viewBox:'0 0 ' + parseInt(x) + ' ' + parseInt(totalHeight)});

function setViewPortFullScreen(duration) {
    svgArea.animate({viewBox: (cx - 650/2) +' '+ (cy-650/2) + ' ' +  650 + ' ' +  650}, duration);
}

var selectedNode = null;

var setSelectedNode = function (node) {
    if (selectedNode != null && (selectedNode != node || node == null)) {
        selectedNode.circle.attr({strokeWidth: 0});
        var count = selectedNode.nodes.length;
        selectedNode.nodes.forEach(function (child) {
            setTimeout(function () {
                child.rect.animate({width: 0, height: 0}, rectAnimationDuration, null, function () {
                    child.rect.remove();
                });
                child.icon.animate({opacity: 0}, null, function () {
                    child.icon.remove();
                });
                child.text.animate({opacity: 0}, null, function () {
                    child.text.remove();
                });
            }, count * rectAnimationDuration);
            count--;
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

    contributions.forEach(function (contribution) {
        var welcomeNode = {};
        welcomeNode.title = contribution.name;
        welcomeNode.title = welcomeNode.title.split(/ /g)[0];
        welcomeNode.icon = contribution.iconURL;
        welcomeNode.nodes = [];
        contribution.wizards.forEach(function (wizard) {
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

var welcomeNodeArray = loadWelcomeNodes();

function toRadians(angle) {
    return angle * (Math.PI / 180);
}

var angleOffset = getRandomArbitrary(toRadians(360), toRadians(20));
var anglePerMainItem = toRadians(360) / (welcomeNodeArray.length);


var cy = $("#pageRow").height()/ 2;
var cx = $("#pageRow").width() / 2;
var cr = 50;

svgArea.attr({viewBox: '0 0 ' +   $("#pageRow").width() + ' ' +  $("#pageRow").height()});

var centeredMainText;
var addCenteredMainText = function () {
    centeredMainText = svgArea.text(cx, cy, "WSO2 Developsdsder Studio")
        .attr({"text-anchor": "middle", "font-size": "0px"})
        .addClass('title');
};

$('.wso2-logo').css("left",$(".header").width() - $('.wso2-logo').width() - $('.devs-logo').width() - 40);

//$('#pageRow').css("height",$("#root-container").height() - 72 -150);


$( window ).resize(function() {
    $('.wso2-logo').css("left",$(".header").width() - $('.wso2-logo').width() - $('.devs-logo').width() - 40);
//    cy = $("#pageRow").height()/ 2;
//    cx = $("#pageRow").width() / 2;
    //$('#pageRow').css("height",$("#root-container").height() - 72 -150);
    location.reload();
});

var addCenteredText = function (x, y, text, dx, dy) {
    if (dx > 0 && dy > 0) {
        return svgArea.text(x, y, text)
            .attr({"text-anchor": "middle", "font-size": "15px", filter: "", opacity: 0, dx: dx, dy: dy})
            .addClass('title');
    }
    return svgArea.text(x, y, text)
        .attr({"text-anchor": "middle", "font-size": "15px", filter: "", opacity: 0})
        .addClass('title');
};

var centerCircle = svgArea.circle(cx, cy, cr);


var myMatrix = new Snap.Matrix();          // play with scaling before and after the rotate
myMatrix.translate(cx,cy);      // this translate will not be applied to the rotation

Snap.load("wso2.svg", function (element) {
    centerLogo = element.select('svg');
    centerLogo.attr({
        height: 200,
        width: 200,
        x: cx - 100,
        y: cy - 100
    });
    svgArea.append(centerLogo);
});


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
    strokeWidth: 10
}).addClass('centerCircle')
    .animate({r: 100}, animationDuration, null, addCenteredMainText)
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

function setViewPortToChild(childCircle, widthOffset, heightPerchild, noOfChildren) {
    var cx0 = parseInt(childCircle.attr('cx'));
    var cyo = parseInt(childCircle.attr('cy'));
    var cr0 = parseInt(childCircle.attr('r'));
    var width = 4 * cr0 + widthOffset;
    svgArea.animate({viewBox: parseInt(cx0 - (2 * cr0)) + ' ' + parseInt(cyo - (2 * cr0)) + ' ' + width + ' ' + parseInt(2 * cr0 + (heightPerchild * noOfChildren))}, animationDuration);
}

function addImageForWizard(x, y, w, h, dx, dy, wizardId) {
    var imageData = GetWizardIconData(wizardId);
    var imageSrc = "data:image/png;base64," + imageData;
    return svgArea.image(imageSrc, x + dx, y + dy, w, h).attr({opacity: 0});
}

function addImageForChild(childNode) {
    return addImageForWizard(childNode.x, childNode.y, 20, 20, 5, 5, childNode.wizardID);
}

function addTextForChild(childNode) {
    var text = GetWizardDescription(childNode.wizardID);
    return svgArea.text(childNode.x + 40, childNode.y + 20, text)
        .attr({"font-size": "15px", filter: "", opacity: 0})
        .addClass('.title');
}

function getRandomArbitrary(min, max) {
    return Math.random() * (max - min) + min;
}
setViewPortFullScreen(400);

welcomeNodeArray.forEach(function (welcomeNode) {
    var line1Endpoint = getEndpointForPath(angleOffset + count * anglePerMainItem, cr);
    var line2Endpoint = getEndpointForPath(angleOffset + count * anglePerMainItem, 250);
    var line = svgArea.line(cx, cy, line1Endpoint.x, line1Endpoint.y)
        .insertBefore(centerCircle)
        .attr({strokeWidth: 10, stroke: colorBorder, strokeLinecap: "round", opacity: 100, filter: ""})
        .animate({x1: cx, y1: cy, x2: line2Endpoint.x, y2: line2Endpoint.y }, animationDuration);
    welcomeNode.text = addCenteredText(line2Endpoint.x, line2Endpoint.y, welcomeNode.title);
    var circle = svgArea.circle(line1Endpoint.x, line1Endpoint.y, cr)
        .insertBefore(welcomeNode.text)
        .attr({fill: colorOrange, stroke: colorBorder, strokeWidth: 0})
        .hover(function () {
            if (selectedNode != welcomeNode) {
                circle.animate({strokeWidth: 10}, 200);
            }
        }, function () {
            if (selectedNode != welcomeNode) {
                circle.animate({strokeWidth: 0}, 200);
            }
        })
        .animate({ cx: line2Endpoint.x, cy: line2Endpoint.y}, animationDuration, null, function () {
            welcomeNode.text.animate({opacity: 100}, 400);
        })
        .addClass("childCircle");
    line.data("dataNode", welcomeNode);
    circle.data("dataNode", welcomeNode);
    welcomeNode.point = line2Endpoint;
    welcomeNode.line = line;
    welcomeNode.circle = circle;

    var expandNodes = function () {
        var childX0 = parseInt(circle.attr('cx')) + 2 * parseInt(circle.attr('r'));
        var childY0 = parseInt(circle.attr('cy')) - parseInt(circle.attr('r')) / 2;
        var childMargin = 10;
        var childHeight = 30;
        var rectWidth = 300;
        var maxPerColumn = 8;
        if (selectedNode == welcomeNode) {
            setSelectedNode(null);
            setTimeout(
                function () {
                    setViewPortFullScreen(400);
                    showUnselectedNodes();
                },
                    welcomeNode.nodes.length * rectAnimationDuration);
            return;
        }
        setSelectedNode(welcomeNode);
        hideUnselectedNodes();
        setViewPortToChild(circle, rectWidth + 50, childHeight + childMargin, maxPerColumn);
        setTimeout(function () {
            var count = 0;
            welcomeNode.nodes.forEach(function (childNode) {
                var color = colorOrange2;
                if(count % 2 == 0){
                    color = colorOrange;
                }
                childNode.x = childX0;
                childNode.y = childY0;
                childNode.rect = svgArea.rect(childNode.x, childNode.y, 0, 0, 10).attr({fill: color, stroke: colorBorder, strokeWidth: 0}).data("dataNode", childNode).hover(function () {
                    childNode.rect.animate({strokeWidth: 5}, 100);
                }, function () {
                    childNode.rect.animate({strokeWidth: 0}, 100);
                }).click(function () {
                    OpenIDEWizard(childNode.wizardID);
                })
                    .addClass('handPointer');
                childNode.icon = addImageForChild(childNode).hover(function () {
                    childNode.rect.animate({strokeWidth: 5}, 100);
                }, function () {
                    childNode.rect.animate({strokeWidth: 0}, 100);
                }).click(function () {
                    OpenIDEWizard(childNode.wizardID);
                })
                    .addClass('handPointer');
                childNode.text = addTextForChild(childNode).hover(function () {
                    childNode.rect.animate({strokeWidth: 5}, 100);
                }, function () {
                    childNode.rect.animate({strokeWidth: 0}, 100);
                }).click(function () {
                    OpenIDEWizard(childNode.wizardID);
                })
                    .addClass('handPointer');
                setTimeout(function () {
                    childNode.rect.animate({width: rectWidth, height: childHeight}, rectAnimationDuration);
                    childNode.icon.animate({opacity: 100}, rectAnimationDuration);
                    childNode.text.animate({opacity: 100}, rectAnimationDuration);
                }, count * rectAnimationDuration);
                count++;
                childY0 += (childMargin + childHeight);
                if(count % maxPerColumn == 0){
                    childX0 += rectWidth + childMargin;
                    childY0 = parseInt(circle.attr('cy')) - parseInt(circle.attr('r')) / 2 + childMargin;
                    setViewPortToChild(circle, (1+count/maxPerColumn)*rectWidth + (2 * childMargin * count/maxPerColumn), childHeight + childMargin, welcomeNode.nodes.length);
                }
            });
        }, animationDuration + 250);
    };

    circle.click(expandNodes);
    welcomeNode.text.click(expandNodes).hover(function () {
        if (selectedNode != welcomeNode) {
            circle.animate({strokeWidth: 10}, 200);
        }
    }, function () {
        if (selectedNode != welcomeNode) {
            circle.animate({strokeWidth: 0}, 200);
        }
    })

    if( $("#pageRow").width()  < 80){
        location.reload();
    }

    circle.hover(function () {

    }, function () {

    });
    count++;

});



function setOpacityOfLogo(op, dur){
    centerLogo.select('#circle3041').animate({opacity: op}, dur);
    centerLogo.select('#path3043').animate({opacity: op}, dur);
    centerLogo.select('#path3045').animate({opacity: op}, dur);
}


function hideUnselectedNodes() {
    if (selectedNode != null) {
        centerCircle.animate({opacity: 0}, animationDuration);
        setOpacityOfLogo(0, animationDuration);
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
    setOpacityOfLogo(100, animationDuration);
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
        setViewPortFullScreen(400);
        showUnselectedNodes();
    }, animationDuration);
});

