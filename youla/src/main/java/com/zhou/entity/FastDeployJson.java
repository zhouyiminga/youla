package com.zhou.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FastDeployJson {
    private List<Input> inputs = new ArrayList<>();
    private List<Output> outputs = new ArrayList<>();

    @Data
    public class Input {
        private String name = "INPUT";
        private int[] shape = {1, 0, 0, 3};
        private String datatype = "UINT8";
        private List<Integer[][][]> data=new ArrayList<>();
    }

    @Data
    public class Output {
        private String name;
    }

    public FastDeployJson(int imgHeight,int imgWidth, Integer[][][] rgbArray,String outputName) {
        Input input = new Input();
        input.getShape()[1]=imgHeight;
        input.getShape()[2]=imgWidth;
        input.getData().add(rgbArray);
        inputs.add(input);
        Output output = new Output();
        output.setName(outputName);
        outputs.add(output);
    }
}
