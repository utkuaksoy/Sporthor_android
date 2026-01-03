//
//  ProfileSectionHeaderViewModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 16.03.2025.
//

import Foundation

final class ProfileSectionHeaderViewModel {
    
    var tabs: [SegmentItem]?
    var selectedTabIndex: Int
    
    init(tabs: [SegmentItem], selectedTabIndex: Int = 0) {
        self.tabs = tabs
        self.selectedTabIndex = selectedTabIndex
    }
    
    func getTitle(indexPath: IndexPath) -> String? {
        tabs?[safe: indexPath.row]?.title
    }
    
    func getImage(indexPath: IndexPath) -> String? {
        tabs?[safe: indexPath.row]?.image
    }
    
    func numberOfItemsInSection() -> Int {
        tabs?.count ?? .zero
    }
}
