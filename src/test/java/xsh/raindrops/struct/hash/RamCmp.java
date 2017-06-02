package xsh.raindrops.struct.hash;


public class RamCmp {

    static void printSize(Object o) {
    	System.out.printf("类型：%s，占用内存：%.2f MB\n", o.getClass().getSimpleName()/*, sizeof(o) / 1024D / 1024D*/);
    }

    public static void main(String[] args) throws Throwable {
        int size = 30000;

        java.util.Map<Object, Object> javaUtilHashMap = new java.util.HashMap<>();
        for (int i = 0; i < size; javaUtilHashMap.put(i, i), i++) {
        }

        net.openhft.koloboke.collect.map.hash.HashIntIntMap openHftHashIntIntMap =
                net.openhft.koloboke.collect.map.hash.HashIntIntMaps.newUpdatableMap();
        for (int i = 0; i < size; openHftHashIntIntMap.put(i, i), i++) {
        }

        java.util.ArrayList<Object> javaUtilArrayList = new java.util.ArrayList<>();
        for (int i = 0; i < size; javaUtilArrayList.add(i), i++) {
        }

        Integer[] objectArray = new Integer[size];
        for (int i = 0; i < size; objectArray[i] = i, i++) {
        }

        com.carrotsearch.hppc.IntArrayList hppcArrayList = new com.carrotsearch.hppc.IntArrayList();
        for (int i = 0; i < size; hppcArrayList.add(i), i++) {
        }

        int[] primitiveArray = new int[size];
        for (int i = 0; i < size; primitiveArray[i] = i, i++) {
        }

        System.out.println("java.vm.name=" + System.getProperty("java.vm.name"));
        System.out.println("java.vm.version=" + System.getProperty("java.vm.version"));
        System.out.println("容器元素总数：" + size);

        printSize(javaUtilHashMap);
        printSize(openHftHashIntIntMap);
        printSize(javaUtilArrayList);
        printSize(hppcArrayList);
        printSize(primitiveArray);
        printSize(objectArray);
    }
}