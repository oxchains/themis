package com.oxchains.themis.order.entity;/**
 * Created by Luo_xuri on 2017/10/24.
 */
import lombok.Data;

import javax.persistence.*;

/**
 * @author huohuo
 * @create 2017-10-24 10:44
 **/
@Entity
@Table(name = "country")
@Data
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
