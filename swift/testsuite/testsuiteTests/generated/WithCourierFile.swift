import Foundation
import SwiftyJSON

public struct WithCourierFile: Serializable {
    
    public let courierFile: CourierFile?
    
    public init(
        courierFile: CourierFile?
    ) {
        self.courierFile = courierFile
    }
    
    public static func readJSON(json: JSON) throws -> WithCourierFile {
        return WithCourierFile(
            courierFile: try json["courierFile"].optional(.Dictionary).json.map { try CourierFile.readJSON($0) }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let courierFile = self.courierFile {
            dict["courierFile"] = courierFile.writeData()
        }
        return dict
    }
}
