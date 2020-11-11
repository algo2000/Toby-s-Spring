import{point} from './point.js';

export class start_ani_mobile
{
    //생성자
    constructor()
    {
        //body태그에 canvas태그를 추가
        this.canvas = document.createElement('canvas');
        this.ctx = this.canvas.getContext("2d");
        //body 태그에 캔버스 추가
        //document.body.appendChild(this.canvas);
        document.getElementById("js").appendChild(this.canvas);

        //리사이즈 이벤트 화면 크기 변화시
        window.addEventListener('resize', this.resize.bind(this),false);
        //스크린 사이즈를 가져옴
        this.resize();

        this.isCanvasVisible = true;

        this.pos = new point();
        this.downPos = new point();
        this.setDownPos = new point();
        this.mousePos = new point();
        this.mousePos2 = new point();

        this.isDown = false;
        this.isAuto = false;

        this.maxSize = 0;
        this.target = null;
        this.radius = 0;
        this.prevRadius = 0;
        this.speed = 0;

        requestAnimationFrame(this.animate.bind(this));
        addEventListener('touchstart',this.onDown.bind(this),false);
        addEventListener('touchmove',this.onMove.bind(this),false);
        addEventListener('touchend',this.onUp.bind(this),false);
    }

    //스크린 사이즈를 가져옴
    resize()
    {
        this.stageWidth = document.body.clientWidth;
        this.stageHeight = document.body.clientHeight;

        this.canvas.width = this.stageWidth*2;
        this.canvas.height = this.stageHeight*2;
        this.ctx.scale(2,2);
    }

    animate(t)
    {

        this.ctx.clearRect(0,0,this.stageWidth,this.stageHeight);

        if(this.isCanvasVisible)
        {
            requestAnimationFrame(this.animate.bind(this));

            let gra = this.ctx.createLinearGradient(0, 0, this.stageWidth, this.stageHeight);
            gra.addColorStop(0, '#FFA953');
            gra.addColorStop(1, '#FF7474');
            this.ctx.fillStyle = gra;

            this.ctx.rect(0, 0, this.stageWidth, this.stageHeight);
            this.ctx.fill();
            
            if(this.target && !this.isAuto)
            {
                const move = this.target.clone().subtract(this.pos).reduce(0.15);
                this.pos.add(move);
                this.centerPos = this.pos.clone().add(this.mousePos2);
                this.radius = this.distanceCal(this.downPos,this.centerPos);

                this.speed = this.radius - this.prevRadius;

                this.prevRadius  = this.radius;

                if(this.speed >= 15)
                {
                    this.setDownPos = this.downPos;
                    this.isAuto = true;
                    this.maxSize = Math.max(this.distanceCal(this.setDownPos,new point(0,0)),this.distanceCal(this.setDownPos,new point(this.stageWidth,0)),this.distanceCal(this.setDownPos,new point(0,this.stageHeight)),this.distanceCal(this.setDownPos,new point(this.stageWidth,this.stageHeight)));
                }

                this.setClip(this.downPos);
            }

            if(this.isAuto)
            {
                if(this.radius>this.maxSize)
                {
                    this.canvas.style.zIndex = - 100;
                    this.isCanvasVisible = false;
                }
                else
                {
                    this.setClip(this.setDownPos,this.speed);
                }
            }

            if(this.radius>=0 && !this.isDown)
            {
                this.setClip(this.downPos,-10);
            }
        }
    }

    setClip(point,radiusDelta = 0)
    {
        this.ctx.clearRect(0,0,this.stageWidth,this.stageHeight);

        let gra = this.ctx.createLinearGradient(0, 0, this.stageWidth, this.stageHeight);
        gra.addColorStop(0, '#FFA953');
        gra.addColorStop(1, '#FF7474');
        this.ctx.fillStyle = gra;

        let region = new Path2D();
        region.rect(0, 0, this.stageWidth, this.stageHeight);
        region.arc(point.x, point.y, this.radius, 0, Math.PI*2);
        this.radius += radiusDelta;
        this.ctx.fill(region, 'evenodd');
    }

    distanceCal(point1,point2)
    {
        const result = Math.sqrt(Math.pow(point2.x - point1.x,2) + Math.pow(point2.y - point1.y,2));
        return result;
    }
    onDown(e)
    {
        this.mousePos.x = e.touches[0].clientX;
        this.mousePos.y = e.touches[0].clientY;

        this.startPos = this.mousePos.clone(); 
        this.downPos = this.mousePos.clone(); 
        this.pos = this.mousePos.clone(); 
        this.mousePos2 = this.mousePos.clone().subtract(this.pos);

        this.isDown = true;   
    }

    onMove(e)
    {
        if(this.isDown)
        {
            this.mousePos.x = e.touches[0].clientX;
            this.mousePos.y = e.touches[0].clientY;

            this.target = this.startPos.clone().add(this.mousePos).subtract(this.downPos);
        }
    }
    onUp(e)
    {
        this.target = null;
        this.isDown = false;
    }
}