package com.learning.exam.model.result;

import com.learning.exam.framework.enums.QlevelEnum;
import com.learning.exam.framework.enums.converter.QlevelEnumConverter;
import com.learning.exam.model.vo.QuestionContentVo;
import lombok.Data;

import java.security.SecureRandom;
import java.util.*;

/**
 * @author liuzihao
 * @date 2019-03-01  17:56
 */
@Data
public class PaperRandomVo {
    private Random random;
    private Integer questionNum;
    private Map<Integer,QuestionContentVo> simpleMap;
    private Map<Integer,QuestionContentVo> generalMap;
    private Map<Integer,QuestionContentVo> mediumMap;
    private Map<Integer,QuestionContentVo> littleDifficultMap;
    private Map<Integer,QuestionContentVo> moreDifficultMap;

    public PaperRandomVo(Integer questionNum) {
        this.random = new SecureRandom();
        this.questionNum = questionNum;
        this.generalMap = new HashMap<>(questionNum);
        this.mediumMap = new HashMap<>(questionNum);
        this.littleDifficultMap = new HashMap<>(questionNum);
        this.moreDifficultMap = new HashMap<>(questionNum);
        this.simpleMap = new HashMap<>(questionNum);
    }
    public void addOne(QlevelEnum qlevelEnum,QuestionContentVo questionContentVo){
        switch (qlevelEnum){
            case SIMPLE:{
                this.simpleMap.put(simpleMap.size()+1,questionContentVo);
                break;
            }
            case GENERAL:{
                this.generalMap.put(generalMap.size()+1,questionContentVo);
                break;
            }
            case MEDIUM:{
                this.mediumMap.put(mediumMap.size()+1,questionContentVo);
                break;
            }
            case LITTLE_DIFFICULT:{
                this.littleDifficultMap.put(littleDifficultMap.size()+1,questionContentVo);
                break;
            }
            case MORE_DIFFICULT:{
                this.moreDifficultMap.put(moreDifficultMap.size()+1,questionContentVo);
                break;
            }
            default:{
                break;
            }
        }
    }
    public Integer[] produceScale(Integer levelScaleSum,List<Integer> levelScale){
        Integer[] scale = new Integer[levelScale.size()];
        if(levelScaleSum==0&&questionNum/5==0){
            levelScale.toArray(scale);
        }else if(levelScaleSum == 0){
            Arrays.fill(scale,questionNum/5);
            for (int i = 0; i < questionNum%5; i++) {
                ++scale[i];
            }
        }else{
            int sum=0;
            for (int i = 0; i < 5; i++) {
                scale[i]=(int)((levelScale.get(i)*questionNum)*1.0)/levelScaleSum;
                sum+=scale[i];
            }
            for (int i = 0; i < questionNum-sum; i++) {
                ++scale[i];
            }
        }
        return scale;
    }
    public List<QuestionContentVo> random(Integer[] scale){
        List<QuestionContentVo> target = new ArrayList<>(questionNum);
        //差值
        int dvalue=0;
        for (int i=0;i<scale.length;i++){
            if(scale[i]!=null&&scale[i]!=0){
                List<QuestionContentVo> list = new ArrayList<>(scale[i]);
                QlevelEnum qlevelEnum = QlevelEnumConverter.converter(i+1);
                Integer scaleNum = scaleLength(qlevelEnum,scale[i]+dvalue);
                dvalue=scale[i]>scaleNum?scale[i]-scaleNum:0;
                for (;list.size()<scaleNum;){
                    QuestionContentVo question = randomOne(qlevelEnum);
                    if(!list.contains(question)){
                        list.add(question);
                    }
                }
                target.addAll(list);
            }
        }
        for (int i = 0; i < questionNum-target.size(); i++) {
            QlevelEnum qlevelEnum = QlevelEnumConverter.converter((i+1)%5);
            target.add(randomOne(qlevelEnum));
        }
        return target;
    }
    private QuestionContentVo randomOne(QlevelEnum qlevelEnum){
        switch (qlevelEnum){
            case SIMPLE:{
                int idx = this.random.nextInt(simpleMap.size())+1;
                return simpleMap.get(idx);
            }
            case GENERAL:{
                int idx = this.random.nextInt(generalMap.size())+1;
                return generalMap.get(idx);
            }
            case MEDIUM:{
                int idx = this.random.nextInt(mediumMap.size())+1;
                return mediumMap.get(idx);
            }
            case LITTLE_DIFFICULT:{
                int idx = this.random.nextInt(littleDifficultMap.size())+1;
                return littleDifficultMap.get(idx);
            }
            case MORE_DIFFICULT:{
                int idx = this.random.nextInt(moreDifficultMap.size())+1;
                return moreDifficultMap.get(idx);
            }
            default:{
                return null;
            }
        }
    }
    private Integer scaleLength(QlevelEnum qlevelEnum,Integer scaleNum){
        switch (qlevelEnum){
            case SIMPLE:{
                if(scaleNum>simpleMap.size()){
                    return simpleMap.size();
                }
                return scaleNum;
            }
            case GENERAL:{
                if(scaleNum>generalMap.size()){
                    return generalMap.size();
                }
                return scaleNum;
            }
            case MEDIUM:{
                if(scaleNum>mediumMap.size()){
                    return mediumMap.size();
                }
                return scaleNum;
            }
            case LITTLE_DIFFICULT:{
                if(scaleNum>littleDifficultMap.size()){
                    return littleDifficultMap.size();
                }
                return scaleNum;
            }
            case MORE_DIFFICULT:{
                if(scaleNum>moreDifficultMap.size()){
                    return moreDifficultMap.size();
                }
                return scaleNum;
            }
            default:{
                return scaleNum;
            }
        }
    }
}
