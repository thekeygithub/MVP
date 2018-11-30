/*
结点类型定义。使用本程序构造树形结构，需要用id和parentId来约束节点之间的父子关系。
在树形结构内部，采用二叉树来表示树形结构。同时每个节点加一个_parent属性，用于表示指向父节点的指针。
程序内部使用顺序表作为二叉树的存储结构，二叉树节点的_order属性记录了本节点在数组中的位置。
*/
function Node(id,parentId,data,link,event,useBox,isBranch,value)
{
	this._data=data;		//节点的值，可以是任何符合HTML规范的字符。如果中间需要用到引号，请使用单引号。使用双引号可能会造成不可预期的问题。
	
	this._id=id;			//本节点的id值。
	
	this._parentId=parentId;//父节点的id值
	
	this._left=null;		//左孩子节点
	
	this._right=null;		//右孩子节点——应用层为该节点的兄弟节点。
	
	this._parent=null;		//父节点
	
	this._order=0;			//该节点在节点数组中的下标。
	
	this._showChild=true;	//是否显示孩子节点。在实现树形结构的动态展示的时候，用这个属性来控制子节点的展开和关闭。
	
	this._link=link;		//点击当前节点进入哪个链接
	
	this._value=value;		//结点代表的值。这是后加的属性，为了解决checkbox选择的值不是id的问题。
	
	this.event=event;		//事件列表
	
	if(useBox!=null)
		this._useCheckBox=useBox;	//本节点前是否使用复选框。当树的userCheckBox为true时本设置才有效
	else
		this._useCheckBox=true;	
	if(isBranch!=null)				//本节点是否是逻辑上的树枝节点。如果某个节点即使没有子节点也不作为子节点对待，应把本属性设置为true。
									//比如“班级-学生”这样逻辑关系的树上，即使某个班级没有学生，在逻辑上，它也不是叶子节点。
		this._isLogicalBranch=isBranch;
	else
		this._isLogicalBranch=false;		
	
}
/*
	树形结构的构造函数。
	本构造函数接受一个treeName参数。
	在实际使用的时候，树的变量名要和传入的树名一致。
*/
function Tree(treeName)
{
	this.root=new Node(-1,-1,"ROOT");//默认的根节点，id为-1，节点数据为ROOT
	this.root._order=-1;
	
	this.nodes=[];				//存储节点的顺序表
	
	this._treeName=treeName;	//树名。
	
	this._treeMade=false;		/*树形结构是否已经构建。
								 *在输出树形结构的时候，这个标识用于判断makeTree方法是否被调用过。
								 *果用户在添加完节点之后没有调用makeTree方法来建造树，程序自动调用建树方法。
								 */
	
	this.config=				//树形结构的配置信息
	{
		base	:"icons",		//存储树形结构图标的目录
		useLine :true,			//是否显示节点前的线
		useIcon	:true,			//是否显示节点前的文件夹图标
		useLink :true,			//是否把节点名作为链接
		target :null,			//目标窗口
		closeBrother:false,		//打开本节点的时候，是否同时关闭它的兄弟节点
		cascade:false,			//是否级联。如果级联为true，则打开（或关闭）本节点同时打开（或关闭）所有子节点
		useCheckBox:false,		//是否在树上添加复选框，实现子树全选功能
		useCheckCascade:true,	//点击复选框是否同时选中或者取消子结点
		boxName:"selectbox",	//useCheckBox为true时有效，复选框的名字。id由树形结构自动设置。
		showRoot:true,			//是否显示根结点。有些时候不显示根节点：比如系统菜单
		useControl:false,		//是否显示控制图片，即加号和减号
		withState:false,		//是否需要设置状态
		checkParent:false		//是否选择父结点；useCheckCascade为true的时候才有效
	};
	
	this.icon = 				//树形结构的图标
	{
		root		:(this.config.base!==null)?this.config.base+"/root.gif":"icons/root.gif",
		
		branch	:(this.config.base!==null)?this.config.base+"/branch.gif":"icons/branch.gif",
		
		branchOpen	:(this.config.base!==null)?this.config.base+"/branchOpen.gif":"icons/branchOpen.gif",
		
		leaf		:(this.config.base!==null)?this.config.base+"/leaf.gif":"icons/leaf.gif",
		
		line		:(this.config.base!==null)?this.config.base+"/line.gif":"icons/line.gif",
		
		join		:(this.config.base!==null)?this.config.base+"/join.gif":"icons/join.gif",
		
		joinBottom	:(this.config.base!==null)?this.config.base+"/joinbottom.gif":"icons/joinbottom.gif",
		
		plus		:(this.config.base!==null)?this.config.base+"/plus.gif":"icons/plus.gif",
		
		plusBottom	:(this.config.base!==null)?this.config.base+"/plusbottom.gif":"icons/plusbottom.gif",
		
		minus		:(this.config.base!==null)?this.config.base+"/minus.gif":"icons/minus.gif",
		
		minusBottom	:(this.config.base!==null)?this.config.base+"/minusbottom.gif":"icons/minusbottom.gif",
		
		nolinePlus	:(this.config.base!==null)?this.config.base+"/nolines_plus.gif":"icons/nolines_plus.gif",
		
		nolineMinus	:(this.config.base!==null)?this.config.base+"/nolines_minus.gif":"icons/nolines_minus.gif",
		
		empty	:(this.config.base!==null)?this.config.base+"/empty.gif":"icons/empty.gif"
	};
}
/*
	添加自己的根节点。默认根节点的id和值分别为-1和ROOT。
	如果用户希望加入自己的跟节点，可以使用本方法。
*/
Tree.prototype.setRoot=function(id,data)
{
	this.root=new Node(id,"",data);
	this.root._order=-1;
	this.root._link="";
};
Tree.prototype.setWithState=function(withState){
	this.config.withState=withState;
};
Tree.prototype.setCheckParent=function(checkParent){
	this.config.checkParent=checkParent;
};
Tree.prototype.setTarget=function(target){
	this.config.target=target;
};
Tree.prototype.showRoot=function(showRoot){
	this.config.showRoot=showRoot;
};
Tree.prototype.useControl=function(useControl){
	this.config.useControl=useControl;
};
Tree.prototype.setImagePath=function(base)
{
	this.config.base=base;
	this.icon = 				//树形结构的图标
	{
		root		:(this.config.base!==null)?this.config.base+"/root.gif":"icons/root.gif",
		
		branch	:(this.config.base!==null)?this.config.base+"/branch.gif":"icons/branch.gif",
		
		branchOpen	:(this.config.base!==null)?this.config.base+"/branchOpen.gif":"icons/branchOpen.gif",
		
		leaf		:(this.config.base!==null)?this.config.base+"/leaf.gif":"icons/leaf.gif",
		
		line		:(this.config.base!==null)?this.config.base+"/line.gif":"icons/line.gif",
		
		join		:(this.config.base!==null)?this.config.base+"/join.gif":"icons/join.gif",
		
		joinBottom	:(this.config.base!==null)?this.config.base+"/joinbottom.gif":"icons/joinbottom.gif",
		
		plus		:(this.config.base!==null)?this.config.base+"/plus.gif":"icons/plus.gif",
		
		plusBottom	:(this.config.base!==null)?this.config.base+"/plusbottom.gif":"icons/plusbottom.gif",
		
		minus		:(this.config.base!==null)?this.config.base+"/minus.gif":"icons/minus.gif",
		
		minusBottom	:(this.config.base!==null)?this.config.base+"/minusbottom.gif":"icons/minusbottom.gif",
		
		nolinePlus	:(this.config.base!==null)?this.config.base+"/nolines_plus.gif":"icons/nolines_plus.gif",
		
		nolineMinus	:(this.config.base!==null)?this.config.base+"/nolines_minus.gif":"icons/nolines_minus.gif",
		
		empty	:(this.config.base!==null)?this.config.base+"/empty.gif":"icons/empty.gif"
	};
}
/*
	配置是否使用线型
	useLine：true|false
*/
Tree.prototype.useLine=function(useLine)
{
	this.config.useLine=useLine;
};
/*
	配置是否使用节点前的图标
	useIcon：true|false
*/
Tree.prototype.useIcon=function(useIcon)
{
	this.config.useIcon=useIcon;
};
/*
	配置是否给节点名加链接
	useLink: true|false
*/
Tree.prototype.useLink=function(useLink)
{
	this.config.useLink=useLink;
};
/*
	配置是否使用级联操作
	cascade: true|false
*/
Tree.prototype.useCascade=function(cascade)
{
	this.config.cascade=cascade;
};
/*
	配置是否使用复选框
	useCheckBox: true|false
*/
Tree.prototype.setCheckBox=function(useCheckBox,boxName)
{
	this.config.useCheckBox=useCheckBox;
	this.config.boxName=boxName;
};
/*
	配置复选框是否使用级联操作
*/
Tree.prototype.useCheckCascade=function(checkCascade)
{
	this.config.useCheckCascade=checkCascade;
};

/*
	配置打开本节点的时候是否同时关闭兄弟节点
	cb:true|false
*/
Tree.prototype.closeBrother=function(cb)
{
	this.config.closeBrother=cb;
}
/*
	构造树形结构。构建树形结构的思路是：
	1、从root节点出发，在节点数组中查找它的第一个孩子节点，挂到他的左子树。
	2、查找其它孩子节点，把他们挂到其左孩子的右子树上。
	3、递归构建其左子树
	4、递归构建其右子树
*/
Tree.prototype.makeTree=function(node)
{
	if(!node)
		return ;
	var temp=null;
	for(var i=0;i<this.nodes.length;i+=1)//查找孩子节点
	{
		if(this.nodes[i]._parentId==node._id)
		{
			if(temp)//不是第一个孩子，挂到左孩子的右子树
			{
				temp._right=this.nodes[i];
				this.nodes[i]._parent=temp;
				temp=temp._right;
			}
			else	//是第一个孩子，挂到本节点的左子树
			{
				node._left=this.nodes[i];
				temp=node._left;
				this.nodes[i]._parent=node;
			}
		}
	}
	if(node._left)//递归构建左子树
		this.makeTree(node._left);
	if(node._right)//递归构建右子树
		this.makeTree(node._right);
	this._treeMade=true;
};
/**
	向树形结构中添加节点。
	这里只是把节点放到节点数组中，在调用makeTree的时候才去建立真正的二叉树结构
*/
Tree.prototype.add=function(id,parentId,data,link,event,useBox,value)
{
	var newNode=new Node(id,parentId,data,link,event,useBox,"",value);
	newNode._order=this.nodes.length;
	this.nodes[this.nodes.length]=newNode;
};
/*
	输出树形结构
*/
Tree.prototype.toString=function()
{
	if(!this._treeMade)//如果二叉树尚未创建，就创建它。
		this.makeTree(this.root);
	return this.nodeString(this.root,0);
}
/*
	输出指定节点
*/
Tree.prototype.nodeString=function(node)
{
	if(!node)
		return "";
	var str="";
	if(node._order!=-1||this.config.showRoot)
		str+=this.getIndent(node)+this.getNodeData(node)+"<br>\n";
	/*
		本节点有左孩子，就输出一个<div>并把它的id赋值为child_+左孩子节点的_order。
		changeChildrenShow方法可以通过这个div的id值来控制它的style.show属性，实现子树的展开和收缩。
	*/
	if(node._left)
	{
		str+="<div id=child_"+node._order+">"
		str+=this.nodeString(node._left);
		str+="</div>"
	}
	if(node._right)
		str+=this.nodeString(node._right);
	return str;
}
/*
	输出节点的值。如果配置了给节点添加链接，即config.useLink为true，就给节点加上链接，否则直接返回节点的值。
*/
Tree.prototype.getNodeData=function(node)
{
	if(this.config.useLink&&node._link)
	{
		if(this.config.target)
			return "<span name='tree_node' id='tree_node_"+node._order+"' onclick='"+this._treeName+".nodeClick("+node._order+");'><a class='a0' href=\""+node._link+"\" target='"+this.config.target+"'>"+node._data+"</a></span>";
		else
			return "<span name='tree_node' id='tree_node_"+node._order+"' onclick='"+this._treeName+".nodeClick("+node._order+");'><a  class='a0' href=\""+node._link+"\">"+node._data+"</a></span>";
	}
	if(node._order!=-1){
		if(node._left)
			return "<span name='tree_node' id='tree_node_"+node._order+"' onclick='"+this._treeName+".nodeClick("+node._order+");'><label class='tree_node' style='cursor:pointer' onclick='javascript:"+this._treeName+".showChildren("+node._order+")'>"+node._data+"</label></span>";
		else
			return "<span name='tree_node' id='tree_node_"+node._order+"' onclick='"+this._treeName+".nodeClick("+node._order+");'><label class='tree_node_without_pointer'>"+node._data+"</label></span>";
	}
	else
		return "<span name='tree_node' id='tree_node_"+node._order+"' onclick='"+this._treeName+".nodeClick("+node._order+");'>"+node._data+"</span>";
}
/*
	输出节点前的缩进占位符和伸缩按钮。
*/
Tree.prototype.getIndent=function(node)
{
	if(node==this.root)
		return "<image src='"+this.icon.root+"'>";
	var indent="";
	if(this.config.useLine){
		indent+=this.getLines(node,"")
		if(this.config.useControl)
			indent+=this.getLineOperator(node);
	}
	else{
		indent+=this.getNolineIndent(node)
		if(this.config.useControl)
			indent+=this.getNolineOperator(node);
	}
	if(this.config.useIcon)
		indent+=this.getIcon(node);
	if(this.config.useCheckBox)
		indent+=this.getCheckBox(node);
	return indent;
}
/*
	输出线型占位符。
	对于线型占位符而言，在逻辑结构中，只有它有兄弟的时候，才应该输出，否则只要留出空位就可以了，而不能输出虚线——否则会有尾巴。
	对应在我们的二叉树结构中，就是当本节点是父节点的左孩子，并且父节点有右孩子的时候，才输出虚线图标。
*/
Tree.prototype.getLines=function(node,subLines)
{
	if(node==this.root)
		return subLines;
	var lines=subLines;
	var temp=node;
	while(temp._parent!=this.root&&temp==temp._parent._right)
		temp=temp._parent;
	if(temp._parent!=this.root)
	{
		if(temp._parent._right)
			lines="<image src='"+this.icon.line+"'>"+lines;
		else
			lines="<image src='"+this.icon.empty+"'>"+lines;
		return this.getLines(temp._parent,lines);
	}
	else
		return lines;
}
/*
	树形线型图标操作符。
*/
Tree.prototype.getLineOperator=function(node)
{
	var lineOperator="";
	if(node._left)
	{
		if(node._right)
			lineOperator+="<image id='line_"+node._order+"' onclick="+this._treeName+".showChildren("+node._order+") style='cursor:pointer' src='"+this.icon.minus+"'>";
		else
			lineOperator+="<image id='line_"+node._order+"' onclick="+this._treeName+".showChildren("+node._order+") style='cursor:pointer' src='"+this.icon.minusBottom+"'>";
	}
	else
	{
		if(node._right)
			lineOperator+="<image src='"+this.icon.join+"'>";
		else
			lineOperator+="<image src='"+this.icon.joinBottom+"'>";
	}
	return lineOperator;
}
/*
	树形节点前的复选框。
	复选框的name由用户指定，id是该节点在节点数组中的位序。
	非叶子节点的复选框的value为""
	叶子节点的复选框的value为该节点的id
*/
Tree.prototype.getCheckBox=function(node)
{
	if(!node._useCheckBox)
		return "";
	if(node==null||!node._useCheckBox)
		return "";
	//if(node==this.root||node._left!=null||node._right!=null)
		//return "<input type='checkbox' name='"+this.config.boxName+"_branch' id='box_"+node._order+"' value='' onclick='"+this._treeName+".selectChildren("+node._order+")'>"
	var val=node._value?node._value:node._id;
	var boxStr= "<input type='checkbox' name='"+this.config.boxName+"' id='box_"+node._order+"' value='"+val+"' ";
	if(this.config.useCheckCascade)
	{
		boxStr+=" onclick='"+this._treeName+".selectChildren("+node._order+");";
		if(this.config.checkParent)
		{
			boxStr+=this._treeName+".selectParent("+node._order+");";
		}
		boxStr+="'";
	}else{
	  // modify by HYL since 2009-12-15
	  boxStr+=" onclick='h_doSelectBox("+node._order+");' ";
	}
	boxStr+=">";
	return boxStr;
}
/*
	返回不带虚线的占位符
*/
Tree.prototype.getNolineIndent=function(node)
{
	if(node==this.root)
		return "";
	var noLine="";
	var temp=node;
	while(temp._parent!=this.root&&temp==temp._parent._right)
		temp=temp._parent;
	if(temp._parent!=this.root)
	{
		noLine+="<image src='"+this.icon.empty+"'>";
		return noLine+this.getNolineIndent(temp._parent);
	}
	else
		return "";
}
Tree.prototype.getNolineOperator=function(node)
{
	if(node._left)
	{
		return "<image id='line_"+node._order+"' onclick="+this._treeName+".showChildren("+node._order+") style='cursor:pointer' src='"+this.icon.nolineMinus+"'>";
	}
	return "<image src='"+this.icon.empty+"'>";
}
/*
	输出节点前的图标
*/
Tree.prototype.getIcon=function(node)
{
	if(node._left)
		return "<image id='icon_"+node._order+"' onclick="+this._treeName+".showChildren("+node._order+") style='cursor:pointer' src='"+this.icon.branchOpen+"'>";
	else
		return "<image src='"+this.icon.leaf+"'>";
}
/*
	变更子树的显示方式——展开或者隐藏。
	order：int 被点击节点在节点数组中的序号
	show： bool true表示展开，false表示隐藏
*/
Tree.prototype.showChildren=function(order,show)
{
	var node=this.nodes[order];
	var showChild;
	if(show!=null)
		showChild=show;
	else
		showChild=!node._showChild;
	if(showChild&&this.config.closeBrother)
	{
		this.closeBrother(node);
	}
	this.showNode(node,showChild);
	if(this.config.cascade)
		this.showSubTree(node._left,showChild);
	else if(this.config.useCheckBox)
	{
		//this.showSubTreeBox(node._left,showChild);
		this.showSubTreeBox(node._left,true);
	}
		
	
}
function h_doSelectBox(order){
   try{
     var box=document.getElementById("box_"+order);
     h_doSelectBoxPage(box.value,box.checked);
   }catch(e){}
}
/*
	选择复选框的子节点
*/
Tree.prototype.selectChildren=function(order)
{
	var node=this.nodes[order];
	var box=document.getElementById("box_"+order);
	if(box)
		this.selectSubTree(node._left,box.checked);
}
/**
	选择当前节点的父节点
*/
Tree.prototype.selectParent=function(order){
		var curNode=this.nodes[order];
		var box=document.getElementById("box_"+order);
		if(box){
			if(box.checked==true)
				this.selectAllParent(curNode,true);
			else{
				parentNode=this.findParent(curNode);
				if(parentNode&&!this.hasSelectedChild(parentNode)){
					this.selectAllParent(parentNode,false);
				}
			}
		}
}
/**
	获得当前节点的父结点
*/
Tree.prototype.findParent=function(curNode){
	var parentNode=curNode._parent;
	while(parentNode&&parentNode._left!=curNode){
		curNode=parentNode;
		parentNode=curNode._parent;					
	}
	return parentNode;
}
/**
	判断当前结点是否有选中的直接孩子结点
*/
Tree.prototype.hasSelectedChild=function(parentNode){
	var hasChecked=false;
	if(parentNode){
		var firstBrother=parentNode._left;
		box=document.getElementById("box_"+firstBrother._order);
		if(box&&box.checked)
			return true;
		var brother=firstBrother._right;
		while(!hasChecked&&brother){
			box=document.getElementById("box_"+brother._order);
			if(box&&box.checked){
				return true;
			}
			brother=brother._right;
		}
		return hasChecked;
	}
}
/**
	递归选择当前节点的所有父节点
*/
Tree.prototype.selectAllParent=function(node,check){
	if(!node)
		return ;
	if(node._order>=0){
		var box=document.getElementById("box_"+node._order);
		if(box){
			if(check){
				box.checked=true;
				this.selectAllParent(this.findParent(node),true);
			}
			else{
				box.checked=false;
				var parentNode=this.findParent(node);
				if(parentNode&&!this.hasSelectedChild(parentNode))
					this.selectAllParent(parentNode,false);
			}
		}
	}
}
/*
	递归选择本节点的所有子树节点
*/
Tree.prototype.selectSubTree=function(node,checked)
{
	if(node==null)
		return ;
/*	if(node._left&&!node._showChild)
	{
		this.selectSubTree(node._right,checked);
		return ;
	}*/
	var box=document.getElementById("box_"+node._order);
	if(box)
		box.checked=checked;
	this.selectSubTree(node._left,checked);
	this.selectSubTree(node._right,checked);
}
/*
	显示子树。
	node:选中的节点
	show: true表示展开，false表示隐藏
*/
Tree.prototype.showSubTree=function(node,show)
{
	if(!node)
		return ;
	this.showNode(node,show);
	this.showSubTree(node._left,show);
	this.showSubTree(node._right,show);
}
/*
	显示子树的复选框
*/
Tree.prototype.showSubTreeBox=function(node,show)
{
	if(node==null)
		return ;
	if(document.getElementById("box_"+node._order))
			document.getElementById("box_"+node._order).style.display="inline";
	/*if(show)
	{
		if(node._left==null)//是叶子节点
		{
			if(!node._isLogicalBranch)//叶子节点，且不是逻辑枝节点，才显示复选框
			{
				if(document.getElementById("box_"+node._order))
					document.getElementById("box_"+node._order).style.display="inline"; 
			}
			else
			{
				if(document.getElementById("box_"+node._order))
					document.getElementById("box_"+node._order).style.display="none"; 
			}
		}
		else//不是叶子节点，如果节点有逻辑叶子节点，且逻辑叶子节点处于展开状态，则显示复选框
		{
			if(this.haveOpenLogicalLeaf(node,show))
			{
				if(document.getElementById("box_"+node._order))
					document.getElementById("box_"+node._order).style.display="inline"; 
			}
			else
			{
				if(document.getElementById("box_"+node._order))
					document.getElementById("box_"+node._order).style.display="none"; 
			}
		}
	}
	else//节点处于收缩状态，不显示复选框
	{
		if(document.getElementById("box_"+node._order))
		{
			document.getElementById("box_"+node._order).style.display="none"; 
		}
	}
	this.showSubTreeBox(node._left,show);
	this.showSubTreeBox(node._right,show);*/
	this.showSubTreeBox(node._left,true);
	this.showSubTreeBox(node._right,true);
}
/*
	判断本节点是否有逻辑叶子节点
*/
Tree.prototype.haveOpenLogicalLeaf=function(node,show)
{
	/*if(node==null)
		return false;
	else if(node._left==null)
	{
		if(!node._isLogicalBranch)
			return true;
		return false;
	}
	else if(this.config.cascade)
		return this.haveLeafInSubTree(node._left);
	else
		return (this.haveLeafInSubTree(node._left)&&node._showChild);*/
	return true;
}
Tree.prototype.haveLeafInSubTree=function(node)
{
	if(node==null)
		return false;
	else if(node._left==null)
	{
		if(!node._isLogicalBranch)
			return true;
		return haveLeafInSubTree(node_right);
	}
	else 
	{
		var have= this.haveLeafInSubTree(node._left);
		if(!this.config.cascade)
			have=have&&node._showChild;
		if(have)
			return true;
		return this.haveLeafInSubTree(node._right);
	}
}
/*
	显示节点。通过节点的_order属性查找该节点的子树对应的div，设置其style.display属性。
	node:当前操作的节点对象。
	showChild: bool。true-显示子树；false-隐藏子树。
*/
Tree.prototype.showNode=function(node,showChild)
{
	if(!node)
		return ;
/*	if(!showChild&&this.config.useCheckBox)//如果是收缩行为，且使用了复选框，则把收缩节点的复选框设置为不选状态
	{
		var box=document.getElementById("box_"+node._order);
		if(box)
			box.checked=false;
		this.selectSubTree(node._left,false);
	}*/
	node._showChild=showChild;
	var order=node._order;
	var elem;
	if(showChild)
	{
		elem=document.getElementById("child_"+order);
		if(elem)
			elem.style.display="block";
		elem=document.getElementById("line_"+order);
		if(elem)
		{
			if(this.nodes[order]._right)
			{
				if(this.config.useLine)
					elem.src=this.icon.minus;
				else
					elem.src=this.icon.nolineMinus;
			}	
			else
			{
				if(this.config.useLine)
					elem.src=this.icon.minusBottom;
				else
					elem.src=this.icon.nolineMinus;
			}
		}
		elem=document.getElementById("icon_"+order);
		if(elem)
		{
			elem.src=this.icon.branchOpen;
		}
		if(this.haveOpenLogicalLeaf(node)){
			elem=document.getElementById("box_"+node._order);
			if(elem)
				elem.style.display="inline";
		} 
	}
	else
	{
		elem=document.getElementById("child_"+order);
		if(elem)
			elem.style.display="none";
		elem=document.getElementById("line_"+order);
		if(elem)
		{
			if(this.nodes[order]._right)
			{
				if(this.config.useLine)
					elem.src=this.icon.plus;
				else
					elem.src=this.icon.nolinePlus;
			}	
			else
			{
				if(this.config.useLine)
					elem.src=this.icon.plusBottom;
				else
					elem.src=this.icon.nolinePlus;
			}
		}
		elem=document.getElementById("icon_"+order);
		if(elem)
		{
			elem.src=this.icon.branch;
		}
		//if(document.getElementById("box_"+node._order))
		//{
			//document.getElementById("box_"+node._order).style.display="none"; 
		//}
	}
}
Tree.prototype.openAll=function()
{
/*	if(this.root._left)
	{
		var temp=this.root._left;
		this.showSubTree(temp,true);
		while(temp._right)
		{
			temp=temp._right;
			this.showSubTree(temp,true);
		}
	}
*/
	for(var i=0;i<this.nodes.length;i++){
		this.showNode(this.nodes[i],true);
	}

}
Tree.prototype.proxyOpenAll=function(before,after){
	if(before){
		eval(before);
	}
	this.openAll();
	if(after){
		eval(after);
	}
}
Tree.prototype.closeAll=function()
{
/*	if(this.root._left)
	{
		var temp=this.root._left;
		this.showSubTree(temp,false);
		while(temp._right)
		{
			temp=temp._right;
			this.showSubTree(temp,false);
		}
	}
*/
	for(var i=0;i<this.nodes.length;i++){
		this.showNode(this.nodes[i],false);
	}
}
Tree.prototype.proxyCloseAll=function(before,after){
	if(before){
		eval(before);
	}
	this.closeAll();
	if(after){
		eval(after);
	}
}
/*
	收缩当前节点的兄弟节点的子树
*/
Tree.prototype.closeBrother=function(node)
{
	var temp=node;
	while(temp._parent&&temp._parent!=this.root&&temp==temp._parent._right)
	{
		temp=temp._parent;
	}
	while(temp)
	{
		if(temp!=node)
			this.showSubTree(temp,false);
		temp=temp._right;
	}
}
Tree.prototype.setTreeState=function(_order){
	if(this.config.withState){
		for(var i=-1;i<this.nodes.length;i++){
			var elem=document.getElementById("tree_node_"+i);
			if(elem)
				elem.style.backgroundColor='white';
		}
		document.getElementById("tree_node_"+_order).style.backgroundColor='lightgrey';
	}
}
Tree.prototype.nodeClick=function(_order){
	this.setTreeState(_order);
	if(_order>-1){
		var node=this.nodes[_order];
		if(node.event){
			eval(node.event);
		}
	}
}
Tree.prototype.findNodeById=function(id){
	for(var i=0;i<this.nodes.length;i++){
		if(this.nodes[i]._id==id)
			return this.nodes[i];
	}
	return null;
}
/*
var tree=new Tree("tree");
tree.setCheckBox(true,"sss");
tree.setRoot("ss","Root");
tree.add(101,"ss",'101');
tree.add(102,"ss",'102');
tree.add(201,101,'201');
tree.add(202,101,'202');
tree.add(301,201,'301');
tree.add(302,201,'302');
tree.add(103,-1,'103');
tree.add(203,102,'203');
tree.add(303,203,'303');
tree.add(304,203,'304');
//tree.makeTree(tree.root);
document.write(tree);
tree.closeAll();*/