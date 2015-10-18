import Foundation
import SwiftyJSON

struct ReservedClassFieldEscaping {
    
    let json$: String?
    
    let read$: String?
    
    let write$: String?
    
    init(json$: String?, read$: String?, write$: String?) {
        
        self.json$ = json$
        
        self.read$ = read$
        
        self.write$ = write$
    }
    
    static func read(json: JSON) -> ReservedClassFieldEscaping {
        return ReservedClassFieldEscaping(
        json$: json["json"].string,
        read$: json["read"].string,
        write$: json["write"].string)
    }
    func write() -> [String : JSON] {
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
        
        return json
    }
}
