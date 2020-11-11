import{ start_ani_mobile } from "./start_ani_mobile.js";
import{ start_ani } from "./start_ani.js";

window.onload = () =>
{
    if (isMobile())
    {
        // console.log('mobile 접속');
        new start_ani_mobile();
    }
    else
    {
        new start_ani();
    }
}

function isMobile()
{
    return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}