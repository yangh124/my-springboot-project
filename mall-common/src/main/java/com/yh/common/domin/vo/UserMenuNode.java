package com.yh.common.domin.vo;

import com.yh.common.domin.entity.UserMenuEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author : yh
 * @date : 2020/12/18 13:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserMenuNode extends UserMenuEntity {

    private static final long serialVersionUID = -1138785759099935980L;

    List<UserMenuNode> children;


}
