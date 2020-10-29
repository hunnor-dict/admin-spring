package net.hunnor.dict.admin.util;

import java.util.HashMap;
import java.util.Map;

public class Mappings {

  private static Map<Integer, Integer> idMap = new HashMap<>();

  private Mappings() {
  }

  static {
    idMap.put(2310, 2522);
    idMap.put(3560, 81089);
    idMap.put(7199, 7872);
    idMap.put(7979, 8797);
    idMap.put(7980, 8798);
    idMap.put(11003, 12228);
    idMap.put(14443, 16064);
    idMap.put(14569, 16197);
    idMap.put(15202, 16892);
    idMap.put(154616, 16893);
    idMap.put(16388, 18238);
    idMap.put(16389, 18239);
    idMap.put(19677, 22013);
    idMap.put(25495, 28795);
    idMap.put(154618, 28796);
    idMap.put(28276, 32134);
    idMap.put(28728, 32613);
    idMap.put(28928, 32853);
    idMap.put(31605, 35745);
    idMap.put(31606, 35746);
    idMap.put(31777, 35938);
    idMap.put(31778, 35939);
    idMap.put(32110, 36299);
    idMap.put(32111, 36300);
    idMap.put(33936, 38254);
    idMap.put(35727, 40367);
    idMap.put(145128, 156081);
    idMap.put(37441, 42218);
    idMap.put(37725, 42584);
    idMap.put(37830, 42718);
    idMap.put(154619, 42716);
    idMap.put(39528, 44672);
    idMap.put(154620, 44674);
    idMap.put(41384, 46607);
    idMap.put(41918, 47316);
    idMap.put(154621, 47317);
    idMap.put(42892, 48366);
    idMap.put(154622, 48367);
    idMap.put(45807, 83044);
    idMap.put(48752, 54521);
    idMap.put(48755, 54525);
    idMap.put(49265, 55078);
    idMap.put(49269, 55082);
    idMap.put(49270, 55083);
    idMap.put(49424, 55274);
    idMap.put(154635, 55275);
    idMap.put(50734, 159264);
    idMap.put(55619, 62373);
    idMap.put(154636, 159290);
    idMap.put(154637, 159292);
    idMap.put(55622, 62380);
    idMap.put(55624, 159287);
    idMap.put(55645, 62413);
    idMap.put(55646, 62414);
    idMap.put(56623, 63445);
    idMap.put(56622, 63446);
    idMap.put(56636, 63458);
    idMap.put(154638, 63459);
    idMap.put(58586, 65562);
    idMap.put(63806, 71278);
    idMap.put(154639, 71279);
    idMap.put(147459, 158497);
    idMap.put(64152, 71695);
    idMap.put(65072, 72714);
    idMap.put(70169, 78692);
    idMap.put(70170, 78693);
    idMap.put(72574, 81364);
    idMap.put(72643, 81445);
    idMap.put(154640, 81446);
    idMap.put(72708, 81513);
    idMap.put(72734, 81549);
    idMap.put(72799, 81646);
    idMap.put(72818, 81673);
    idMap.put(148956, 159261);
    idMap.put(72820, 81675);
    idMap.put(154641, 81676);
    idMap.put(72823, 81677);
    idMap.put(154643, 81678);
    idMap.put(72827, 81683);
    idMap.put(154644, 81684);
    idMap.put(72839, 81697);
    idMap.put(72936, 81809);
    idMap.put(61422, 68715);
    idMap.put(154645, 68716);
    idMap.put(72944, 81818);
    idMap.put(154646, 156140);
    idMap.put(72963, 81838);
    idMap.put(72964, 81841);
    idMap.put(73016, 81903);
    idMap.put(73024, 81910);
    idMap.put(154647, 81911);
    idMap.put(73068, 81962);
    idMap.put(73069, 81963);
    idMap.put(73070, 81964);
    idMap.put(154648, 82131);
    idMap.put(73103, 82007);
    idMap.put(73149, 81406);
    idMap.put(73377, 17582);
    idMap.put(104392, 114834);
    idMap.put(104921, 115364);
    idMap.put(122723, 55186);
    idMap.put(135957, 69755);
    idMap.put(138770, 72922);
    idMap.put(138832, 72976);
    idMap.put(145110, 38875);
    idMap.put(145115, 156065);
    idMap.put(145350, 142110);
    idMap.put(9229, 10320);
    idMap.put(1338, 1443);
    idMap.put(26531, 30159);
    idMap.put(145137, 44033);
    idMap.put(78074, 88195);
    idMap.put(72606, 8204);
    idMap.put(93221, 22592);
  }

  public static Integer getIdMapping(Integer id) {
    return idMap.get(id);
  }

}
