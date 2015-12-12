import Foundation
import SwiftyJSON

/**
    Scala's tuple limit is 22. Here we test the limit.
*/
public struct With22Fields: Serializable {
    
    public let field1: Int?
    
    public let field2: Int?
    
    public let field3: Int?
    
    public let field4: Int?
    
    public let field5: Int?
    
    public let field6: Int?
    
    public let field7: Int?
    
    public let field8: Int?
    
    public let field9: Int?
    
    public let field10: Int?
    
    public let field11: Int?
    
    public let field12: Int?
    
    public let field13: Int?
    
    public let field14: Int?
    
    public let field15: Int?
    
    public let field16: Int?
    
    public let field17: Int?
    
    public let field18: Int?
    
    public let field19: Int?
    
    public let field20: Int?
    
    public let field21: Int?
    
    public let field22: Int?
    
    public init(
        field1: Int?,
        field2: Int?,
        field3: Int?,
        field4: Int?,
        field5: Int?,
        field6: Int?,
        field7: Int?,
        field8: Int?,
        field9: Int?,
        field10: Int?,
        field11: Int?,
        field12: Int?,
        field13: Int?,
        field14: Int?,
        field15: Int?,
        field16: Int?,
        field17: Int?,
        field18: Int?,
        field19: Int?,
        field20: Int?,
        field21: Int?,
        field22: Int?
    ) {
        self.field1 = field1
        self.field2 = field2
        self.field3 = field3
        self.field4 = field4
        self.field5 = field5
        self.field6 = field6
        self.field7 = field7
        self.field8 = field8
        self.field9 = field9
        self.field10 = field10
        self.field11 = field11
        self.field12 = field12
        self.field13 = field13
        self.field14 = field14
        self.field15 = field15
        self.field16 = field16
        self.field17 = field17
        self.field18 = field18
        self.field19 = field19
        self.field20 = field20
        self.field21 = field21
        self.field22 = field22
    }
    
    public static func readJSON(json: JSON) throws -> With22Fields {
        return With22Fields(
            field1: try json["field1"].optional(.Number).int,
            field2: try json["field2"].optional(.Number).int,
            field3: try json["field3"].optional(.Number).int,
            field4: try json["field4"].optional(.Number).int,
            field5: try json["field5"].optional(.Number).int,
            field6: try json["field6"].optional(.Number).int,
            field7: try json["field7"].optional(.Number).int,
            field8: try json["field8"].optional(.Number).int,
            field9: try json["field9"].optional(.Number).int,
            field10: try json["field10"].optional(.Number).int,
            field11: try json["field11"].optional(.Number).int,
            field12: try json["field12"].optional(.Number).int,
            field13: try json["field13"].optional(.Number).int,
            field14: try json["field14"].optional(.Number).int,
            field15: try json["field15"].optional(.Number).int,
            field16: try json["field16"].optional(.Number).int,
            field17: try json["field17"].optional(.Number).int,
            field18: try json["field18"].optional(.Number).int,
            field19: try json["field19"].optional(.Number).int,
            field20: try json["field20"].optional(.Number).int,
            field21: try json["field21"].optional(.Number).int,
            field22: try json["field22"].optional(.Number).int
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let field1 = self.field1 {
            dict["field1"] = field1
        }
        if let field2 = self.field2 {
            dict["field2"] = field2
        }
        if let field3 = self.field3 {
            dict["field3"] = field3
        }
        if let field4 = self.field4 {
            dict["field4"] = field4
        }
        if let field5 = self.field5 {
            dict["field5"] = field5
        }
        if let field6 = self.field6 {
            dict["field6"] = field6
        }
        if let field7 = self.field7 {
            dict["field7"] = field7
        }
        if let field8 = self.field8 {
            dict["field8"] = field8
        }
        if let field9 = self.field9 {
            dict["field9"] = field9
        }
        if let field10 = self.field10 {
            dict["field10"] = field10
        }
        if let field11 = self.field11 {
            dict["field11"] = field11
        }
        if let field12 = self.field12 {
            dict["field12"] = field12
        }
        if let field13 = self.field13 {
            dict["field13"] = field13
        }
        if let field14 = self.field14 {
            dict["field14"] = field14
        }
        if let field15 = self.field15 {
            dict["field15"] = field15
        }
        if let field16 = self.field16 {
            dict["field16"] = field16
        }
        if let field17 = self.field17 {
            dict["field17"] = field17
        }
        if let field18 = self.field18 {
            dict["field18"] = field18
        }
        if let field19 = self.field19 {
            dict["field19"] = field19
        }
        if let field20 = self.field20 {
            dict["field20"] = field20
        }
        if let field21 = self.field21 {
            dict["field21"] = field21
        }
        if let field22 = self.field22 {
            dict["field22"] = field22
        }
        return dict
    }
}
