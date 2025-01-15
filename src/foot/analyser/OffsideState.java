package foot.analyser;

import org.opencv.core.Scalar;

import ui.constant.ScalarConstants;

public class OffsideState {
    private String state;
    private Scalar offsideColor;
    private OffsideState(String state){
        setState(state);
    }
    public static OffsideState Offside(){
        OffsideState state = new OffsideState("HJ");
        state.setOffsideColor(ScalarConstants.VIOLET());
        return state;
    }
    public static OffsideState InGame(){
        return new OffsideState("M");
    }
    public static OffsideState OutGame(){
        return new OffsideState("");
    }

    public String getState() {
        return state;
    }

    private void setState(String state) {
        this.state = state;
    }
    public Scalar getOffsideColor() {
        return offsideColor;
    }
    public void setOffsideColor(Scalar offsideColor) {
        this.offsideColor = offsideColor;
    }
}
