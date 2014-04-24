function FastButton(a,b){this.lastOver=null;
this.element=a;
this.handler=b;
a.addEventListener("touchstart",this,false);
a.addEventListener("mouseover",this,false)
}FastButton.prototype.handleEvent=function(a){switch(a.type){case"touchstart":this.onTouchStart(a);
break;
case"touchmove":this.onTouchMove(a);
break;
case"touchend":this.onClick(a);
break;
case"click":this.onClick(a);
break;
case"mouseover":this.onMouseOver(a);
break
}};
FastButton.prototype.onTouchStart=function(a){a.stopPropagation();
this.element.addEventListener("touchend",this,false);
document.body.addEventListener("touchmove",this,false);
this.startX=a.touches[0].clientX;
this.startY=a.touches[0].clientY
};
FastButton.prototype.onTouchMove=function(a){if(Math.abs(a.touches[0].clientX-this.startX)>10||Math.abs(a.touches[0].clientY-this.startY)>10){this.reset()
}};
FastButton.prototype.onClick=function(a){a.stopPropagation();
this.reset();
this.handler(a);
if(a.type=="touchend"){FastButtonUtils.preventGhostClick(this.startX,this.startY)
}};
FastButton.prototype.onMouseOver=function(b){var a=this;
a.lastOver=b.toElement||b.target;
setTimeout(function(){a.lastOver=null
},100)
};
FastButton.prototype.reset=function(){this.element.removeEventListener("touchend",this,false);
document.body.removeEventListener("touchmove",this,false)
};
FastButtonUtils=function(){};
FastButtonUtils.preventGhostClick=function(a,b){FastButtonUtils.coordinates.push(a,b);
window.setTimeout(FastButtonUtils.pop,2500)
};
FastButtonUtils.pop=function(){FastButtonUtils.coordinates.splice(0,2)
};
FastButtonUtils.onClick=function(b){var g=false;
var d=b.toElement||b.target;
if(d.getAttribute("data-masqueFastClick")=="true"){return
}for(var e=0;
e<FastButtonUtils.coordinates.length&&!g;
e+=2){var j=FastButtonUtils.coordinates[e];
var h=FastButtonUtils.coordinates[e+1];
if(Math.abs(b.clientX-j)<25&&Math.abs(b.clientY-h)<25){b.stopPropagation();
b.preventDefault();
g=true
}}if(!g){while(d!=null&&d.getAttribute&&d.getAttribute("data-preventghostclick")!=="true"){d=d.parentNode
}if(d!=null&&d.getAttribute&&d.getAttribute("data-preventghostclick")==="true"){if(!document.compteurMasqueFastClick){document.compteurMasqueFastClick=1
}else{document.compteurMasqueFastClick++
}var c=document.compteurMasqueFastClick;
var a=document.getElementById("div-masqueFastClick");
if(!a){a=document.createElement("div");
a.setAttribute("data-masqueFastClick","true");
a.setAttribute("id","div-masqueFastClick");
a.style.position="fixed";
a.style.zIndex="999999";
document.body.appendChild(a)
}a.style.opacity="0";
var f=FastButtonUtils.getCoordinates(d);
a.style.top=f.y+"px";
a.style.left=f.x+"px";
a.style.width=d.scrollWidth+"px";
a.style.height=d.scrollHeight+"px";
setTimeout(function(){if(c==document.compteurMasqueFastClick){a.style.top="0px";
a.style.left="0px";
a.style.width="0px";
a.style.height="0px"
}},1000)
}}};
FastButtonUtils.getCoordinates=function(b){var a=0;
var e=0;
var d=b;
while(d!=null){a+=d.offsetLeft-d.scrollLeft;
e+=d.offsetTop-d.scrollTop;
var c=d.parentNode;
while(c!=null&&c!=d.offsetParent){if(c.scrollTop){e-=c.scrollTop
}if(c.scrollLeft){a-=c.scrollLeft
}c=c.parentNode
}d=d.offsetParent
}return{x:a,y:e}
};
FastButtonUtils.fastClickToRealClick=function(){var a=this.lastOver||document.elementFromPoint(this.startX,this.startY);
if(a.nodeType==3){a=a.parentNode
}var b=document.createEvent("MouseEvents");
b.initEvent("click",true,true);
a.dispatchEvent(b)
};
FastButtonUtils.coordinates=[];
FastButtonUtils.initFastButtons=function(){new FastButton(document.body,FastButtonUtils.fastClickToRealClick)
};
//$(document).ready(FastButtonUtils.initFastButtons);
document.addEventListener( "DOMContentLoaded", function(){FastButtonUtils.initFastButtons}, false);
document.addEventListener("click",FastButtonUtils.onClick,true);