//
//  ProfileViewModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//

import ComponentBaseKit
import Foundation

final class ProfileViewModel {
    
    var profileInfo: ProfileInfoModel?
    private(set) var components: [any CollectionComponentViewModel] = []
    var selectedSegmentType: SegmentType?
    
    var segmentComponentIndex: IndexPath? {
        guard let index = components.firstIndex(where: { $0 is ProfileSegmentComponentViewModel })
        else { return nil }
        return IndexPath(item: .zero, section: index)
    }
    
    var actionButtonsComponentIndex: IndexPath? {
        guard let index = components.firstIndex(where: { $0 is ProfileActionButtonsComponentViewModel })
        else { return nil }
        return IndexPath(item: .zero, section: index)
    }
    
    func update(with response: ProfileResponseModel) {
        profileInfo = response.info
    }
    
    func update(with components: [any CollectionComponentViewModel]) {
        self.components = components
    }
    
    var profileInfoComponentViewModel: ProfileInfoComponentViewModel? {
        components.first(where: { $0 is ProfileInfoComponentViewModel }) as? ProfileInfoComponentViewModel
    }
    
    var getSegmentComponentViewModel: ProfileSegmentComponentViewModel? {
        components.first(where: { $0 is ProfileSegmentComponentViewModel }) as? ProfileSegmentComponentViewModel
    }
}
