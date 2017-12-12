function clickButton(action,target)
{
	if (target == '_top')
		top.location.href = action;
	else
		top.frames[target].location.href = action;
}

function buttonOnMouseOver(target)
{
	if (target.childNodes[1] == null) // IE
		target.childNodes[0].childNodes[1].childNodes[1].style.backgroundColor = "#B5D2EE";
	else // FireFox
		target.childNodes[1].childNodes[2].childNodes[1].style.backgroundColor = "#B5D2EE";
}

function buttonOnMouseOut(target)
{
	if (target.childNodes[1] == null) // IE
		target.childNodes[0].childNodes[1].childNodes[1].style.backgroundColor = "#E2F4FD";
	else // FireFox
		target.childNodes[1].childNodes[2].childNodes[1].style.backgroundColor = "#E2F4FD";
}

function menubuttonTurnOn(FRM,BTN)
{
	if (document.getElementById(BTN) == null)
	{
		//alert("window.document.forms");
		window.document.forms[FRM].elements[BTN].style.backgroundColor = "#E2F4FD";
		window.document.forms[FRM].elements[BTN].style.borderColor = "#3868A9";
	}
	else
	{
		//alert("document.getElementById");
		document.getElementById(BTN).style.backgroundColor = "#E2F4FD";
		document.getElementById(BTN).style.borderColor = "#3868A9";
		
	}
}

function menubuttonTurnOff(FRM,BTN)
{
	if (document.getElementById(BTN) == null)
	{
		window.document.forms[FRM].elements[BTN].style.backgroundColor = "#FFFFFF";
		window.document.forms[FRM].elements[BTN].style.borderColor = "#FFFFFF";
	}
	else
	{
		document.getElementById(BTN).style.backgroundColor = "#FFFFFF";
		document.getElementById(BTN).style.borderColor = "#FFFFFF";
	}
}

