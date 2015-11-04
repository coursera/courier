import Foundation
import SwiftyJSON

public struct ReservedClassFieldEscaping: JSONSerializable {
    
    public let json$: String?
    
    public let read$: String?
    
    public let write$: String?
    
    public init(
        json$: String?,
        read$: String?,
        write$: String?
    ) {
        self.json$ = json$
        self.read$ = read$
        self.write$ = write$
    }
    
    public static func read(json: JSON) -> ReservedClassFieldEscaping {
        return ReservedClassFieldEscaping(
            json$: json["json"].string,
            read$: json["read"].string,
            write$: json["write"].string
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let json$ = self.json$ {
            json["json"] = JSON(json$)
        }
        if let read$ = self.read$ {
            json["read"] = JSON(read$)
        }
        if let write$ = self.write$ {
            json["write"] = JSON(write$)
        }
        return JSON(json)
    }
}
