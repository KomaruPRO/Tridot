package pro.komaru.tridot.util.comps.render.gui;

import pro.komaru.tridot.util.phys.AbsRect;

public interface ICenteredGuiDrawer extends IGuiDrawer {
    @Override
    default IGuiDrawer clipOn(int x, int y, int w, int h) {
        AbsRect r = AbsRect.xywhCent(x,y,w,h).pose(stack());
        graphics().enableScissor((int) r.x, (int) r.y, (int) r.x2, (int) r.y2);
        return this;
    }

    @Override
    default IGuiDrawer rect(String texture, float x, float y, float sclx, float scly, float rotation, float rotpx, float rotpy, int clipX, int clipY, int clipW, int clipH, int tw, int th) {
        push();

        move(x-tw/2f,y-th/2f);
        scale(sclx,scly);
        rotate(rotation,rotpx+tw/2f,rotpy+th/2f);

        graphics().blit(texturePath(texture),
                0,0,clipX,clipY,clipW,clipH,tw,th);

        pop();

        return this;
    }
}
