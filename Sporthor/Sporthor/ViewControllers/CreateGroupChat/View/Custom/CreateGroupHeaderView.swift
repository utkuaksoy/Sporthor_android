//
//  CreateGroupHeaderView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 27.03.2025.
//
//

import UIKit
import ComponentKit
import DesignKit

protocol CreateGroupHeaderViewDelegate: AnyObject {
    func createGroupHeaderView(_ headerView: CreateGroupHeaderView, searchTextDidChange text: String)
}

final class CreateGroupHeaderView: UIView {
    
    // MARK: - Private UI Elements
    
    private lazy var containerStackView: CKStackView = {
        let stackView = CKStackView(
            axis: .vertical,
            distribution: .fill,
            alignment: .fill,
            spacing: 16
        )
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([groupNameView, seperatorView, searchBar])
        return stackView
    }()
    
    private(set) lazy var groupNameView: GroupNameView = {
        let view = GroupNameView()
        view.isHidden = !isNewGroup
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var seperatorView: UIView = {
        let view = UIView()
        view.isHidden = !isNewGroup
        view.backgroundColor = ColorName.borderSoft200.color
        return view
    }()
    
    private lazy var searchBar: CKSearchBar = {
        let searchBar = CKSearchBar(
            delegate: self,
            textColor: DesignKitColorName.contentStrong900.color,
            placeholder: "Kişi ara",
            placeholderColor: DesignKitColorName.contentSoft600.color,
            backgroundColor: DesignKitColorName.contentWeak100.color,
            cornerRadius: 22,
            borderWidth: 1,
            selectedBorderColor: DesignKitColorName.borderSub300.color,
            font: .body04Compact,
            image: Asset.searchbarSearch.image,
            clearImage: Asset.searchbarClose.image,
            cancelButtonTitle: "İptal",
            cancelButtonTitleColor: DesignKitColorName.contentStrong900.color,
            cancelButtonFont: .body04Compact
        )
        searchBar.translatesAutoresizingMaskIntoConstraints = false
        return searchBar
    }()
    
    // MARK: - Properties
    private let isNewGroup: Bool
    weak var delegate: CreateGroupHeaderViewDelegate?
    
    // MARK: - Initializer
    
    init(frame: CGRect, isNewGroup: Bool) {
        self.isNewGroup = isNewGroup
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}

// MARK: - CKSearchBarDelegate
extension CreateGroupHeaderView: CKSearchBarDelegate {
    func searchBarTextDidChange(_ searchBar: CKSearchBar, text: String) {
        delegate?.createGroupHeaderView(self, searchTextDidChange: text)
    }
} 

// MARK: - Setup

private extension CreateGroupHeaderView {
    func setupViews() {
        backgroundColor = .white
        addSubview(containerStackView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerStackView.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 16),
            containerStackView.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -16),
            containerStackView.topAnchor.constraint(equalTo: topAnchor),
            containerStackView.bottomAnchor.constraint(equalTo: bottomAnchor),
            
            groupNameView.heightAnchor.constraint(equalToConstant: 48),
            seperatorView.heightAnchor.constraint(equalToConstant: 1),
            searchBar.heightAnchor.constraint(equalToConstant: 44)
        ])
    }
}
