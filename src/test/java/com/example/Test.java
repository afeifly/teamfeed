package com.example;

import java.util.Random;

public class Test {
    public static int getRandomIconIndex() {
        Random random = new Random();
        return random.nextInt(46) + 1;
    }


    public static String getCuteNickname() {
        String[] cuteNicknames = {
                "爆米花杀手",
                "无敌洗碗机",
                "翻滚吃货",
                "蚊子捕手",
                "皮皮虾大魔王",
                "失落的遥控器",
                "鸡毛蒜皮",
                "豆腐头",
                "呼噜侠",
                "鼠标精灵",
                "半夜吃冰淇淋",
                "不明飞行物",
                "懒洋洋小猪",
                "发廊老板",
                "无聊剁手族",
                "假装在写代码",
                "废寝忘食怪兽",
                "熬夜王子",
                "微笑摸鱼",
                "电视剧控",
                "高冷表情包",
                "偷偷吃零食",
                "面试拒绝王",
                "拖延症患者",
                "电梯乐手",
                "五分钟煮面大师",
                "停不下来的旋转椅",
                "创意发型师",
                "面瘫模特",
                "隐形朋友",
                "魔法热水器",
                "睡不醒的闹钟",
                "脱线设计师",
                "奔跑的香蕉",
                "路痴导航",
                "橡皮擦收藏家",
                "口袋吃货",
                "茶壶摇滚手",
                "咖啡牛奶王子",
                "微笑刷剧",
                "抽风舞者",
                "呆萌小仙女",
                "绕口令冠军",
                "无厘头小丑",
                "睡懒觉冠军",
                "沙发霸主",
                "拖延之王",
                "电脑屏幕忍者",
                "自言自语大师",
                "吃饭领袖",
                "拍照达人",
                "吹牛皮大王",
                "假装工作中",
                "呼噜王子",
                "吃货代表",
                "嘴巴不停机器",
                "忍者发型师",
                "不务正业党",
                "睡觉界拳王",
                "拖延病毒",
                "失踪专家",
                "不务正业队长",
                "思考人生大师",
                "狂吃狂睡",
                "微笑装忙",
                "五分钟起床王",
                "糊涂蛋",
                "自拍怪兽",
                "发呆之王",
                "无聊创意家",
                "电梯舞者",
                "大白兔奶糖",
                "抠脚大汉",
                "捉迷藏高手",
                "自来熊",
                "空调君",
                "口胡大王",
                "半夜吃泡面",
                "失眠党主席",
                "路痴队长",
                "面试杀手",
                "假装在工作",
                "独角戏演员",
                "吃货总统",
                "自言自语小丑",
                "绕口令专家",
                "偷懒发明家",
                "忘事王",
                "不务正业之王",
                "抽风小丑",
                "隐形人",
                "电视剧狂魔",
                "愤怒的键盘侠",
                "茶壶舞者",
                "嘴巴不停歇机器",
                "不务正业之队长",
                "糊涂蛋大师",
                "自拍达人",
                "拍照狂魔",
                "睡懒觉大王",
                "发呆党领袖",
                "思考人生达人",
                "微笑装傻",
                "五分钟起床王子"
        };

        // 输出所有昵称
        for (int i = 0; i < cuteNicknames.length; i++) {
            System.out.println(cuteNicknames[i]);
        }
        int index = new Random().nextInt(cuteNicknames.length);
        return cuteNicknames[index];
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {

            System.out.print(" " + getRandomIconIndex());
        }
        System.out.println("----" + getCuteNickname());
    }
}
