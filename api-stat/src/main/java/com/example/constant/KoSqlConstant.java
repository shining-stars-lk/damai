package com.example.constant;

public class KoSqlConstant {
    public final static String addMethod = "REPLACE INTO ko_method_node(id, name, class_name, method_name, route_name, method_type) VALUES (?, ?, ?, ?, ?, ?)";
    public final static String queryMethod = "SELECT id, name, class_name, method_name, route_name, method_type FROM ko_method_node  WHERE id=?";
    public final static String queryMethodExist = "SELECT id FROM ko_method_node  WHERE id=?";
    public final static String queryMethodLikeName = "SELECT id, name, class_name, method_name, route_name, method_type FROM ko_method_node  WHERE name like ?";
    public final static String queryMethodByType = "SELECT id, name, class_name, method_name, route_name, method_type FROM ko_method_node  WHERE method_type=?";
    public final static String updateMethod = "UPDATE ko_method_node SET name=?, class_name=?, method_name=?, route_name=?, method_type=? WHERE id=?";
    public final static String addException = "REPLACE INTO ko_exception_node(id, name, class_name) VALUES (?, ?, ?)";
    public final static String queryExceptions = "select distinct e.*,r.message from ko_exception_node e " +
            "join ko_exception_relation r on e.id = r.target_id";
    public final static String queryException = "SELECT id, name, class_name FROM ko_exception_node  WHERE id=?";
    public final static String queryExceptionExist = "SELECT id FROM ko_exception_node  WHERE id=?";
    public final static String addMethodRe = "INSERT INTO ko_method_relation(id, source_id, target_id, avg_run_time, max_run_time, min_run_time) VALUES (?, ?, ?, ?, ?, ?)";
    public final static String queryMethodRe = "SELECT id, source_id, target_id, avg_run_time, max_run_time, min_run_time FROM ko_method_relation WHERE id=?";
    public final static String queryMethodReBySource = "SELECT id, source_id, target_id, avg_run_time, max_run_time, min_run_time FROM ko_method_relation WHERE source_id=?";
    public final static String queryMethodReByTarget = "SELECT id, source_id, target_id, avg_run_time, max_run_time, min_run_time FROM ko_method_relation WHERE target_id=?";
    public final static String updateMethodRe = "UPDATE ko_method_relation SET source_id=?, target_id=?, avg_run_time=?, max_run_time=?, min_run_time=? WHERE id=?";
    public final static String addExceptionRe = "INSERT INTO ko_exception_relation(id, source_id, target_id, location,message) VALUES (?, ?, ?, ?, ?)";
    public final static String queryExceptionRe = "SELECT id, source_id, target_id, location, message FROM ko_exception_relation WHERE id=?";
    public final static String queryExceptionReExist = "SELECT id FROM ko_exception_relation WHERE id=?";
    public final static String queryExceptionReBySource = "SELECT id, source_id, target_id, location, message FROM ko_exception_relation WHERE source_id=?";
    public final static String queryExceptionReByTargetAndMessage = "SELECT id, source_id, target_id, location, message FROM ko_exception_relation WHERE target_id=? and message=?";
    public final static String addParamsAna = "INSERT INTO ko_param_ana (source_id, params, avg_run_time, max_run_time, min_run_time) VALUES (?, ?, ?, ?, ?)";
    public final static String queryParamsAna = "SELECT source_id, params, avg_run_time, max_run_time, min_run_time FROM ko_param_ana WHERE source_id=? and params=?";
    public final static String queryParamsAnaBySource = "SELECT source_id, params, avg_run_time, max_run_time, min_run_time FROM ko_param_ana WHERE source_id=?";
    public final static String updateParamsAna = "UPDATE ko_param_ana SET avg_run_time=?, max_run_time=?, min_run_time=?  WHERE source_id=? and params=?";


    public final static String queryControllers = "select m.id,name,class_name,method_name,method_type,route_name,r.avg_run_time,r.max_run_time,r.min_run_time " +
            "from ko_method_node m " +
            "join ko_method_relation r on m.id = r.target_id " +
            "where m.method_type='Controller'";

    public final static String searchMethodsByName = "select m.id,name,class_name,method_name,method_type,route_name,r.avg_run_time,r.max_run_time,r.min_run_time " +
            "from ko_method_node m " +
            "join ko_method_relation r on m.id = r.target_id " +
            "where m.name like ?";

    public final static String queryChildrenByParent ="select m.id,name,class_name,method_name,method_type,route_name,r.avg_run_time,r.max_run_time,r.min_run_time " +
            "from ko_method_node m " +
            "join ko_method_relation r on m.id = r.target_id " +
            "where r.source_id=?";

}
