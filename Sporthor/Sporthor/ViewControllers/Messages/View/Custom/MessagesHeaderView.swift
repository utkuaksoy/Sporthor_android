//
//  MessagesHeaderView.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 26.03.2025.
//

import ComponentKit
import DesignKit
import UIKit

protocol MessagesHeaderViewDelegate: AnyObject {
    func didTapAddButton()
    func didSearchTextChange(_ text: String)
}

final class MessagesHeaderView: UIView {
    
    // MARK: - Private UI Elements
    
    private lazy var titleLabel: UILabel = {
        let label = CKLabel(
            text: "Mesajlar",
            textColor: ColorName.contentStrong900.color,
            font: .heading05
        )
        label.setContentHuggingPriority(.required, for: .horizontal)
        label.setContentCompressionResistancePriority(.required, for: .horizontal)
        return label
    }()
    
    private lazy var addButton: CKButton = {
        let button = CKButton(
            delegate: self,
            buttonBackgroundColor: ColorName.contentStrong900.color,
            cornerRadius: 15,
            isEnabled: true,
            image: Asset.headerAddButton.image
        )
        button.clipsToBounds = true
        button.tintColor = DesignKitColorName.borderStrong900.color
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var titleStackView: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [titleLabel, addButton])
        stackView.axis = .horizontal
        stackView.spacing = 4
        stackView.alignment = .center
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var searchBar: CKSearchBar = {
        let searchBar = CKSearchBar(
            delegate: self,
            textColor: DesignKitColorName.contentStrong900.color,
            placeholder: "Mesajlarda ara",
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
    
    private lazy var containerStackView: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [titleStackView, searchBar])
        stackView.axis = .vertical
        stackView.spacing = 8
        stackView.alignment = .fill
        stackView.distribution = .fill
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Private Properties
    
    private weak var delegate: MessagesHeaderViewDelegate?
    
    // MARK: - Init
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupViews()
        setupConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func configure(delegate: MessagesHeaderViewDelegate?) {
        self.delegate = delegate
    }
}

// MARK: - Setup

private extension MessagesHeaderView {
    func setupViews() {
        addSubview(containerStackView)
    }
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            containerStackView.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 16),
            containerStackView.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -16),
            containerStackView.topAnchor.constraint(equalTo: topAnchor, constant: 12),
            containerStackView.bottomAnchor.constraint(equalTo: bottomAnchor),
            
            addButton.widthAnchor.constraint(equalToConstant: 30),
            addButton.heightAnchor.constraint(equalToConstant: 30),
            
            searchBar.heightAnchor.constraint(equalToConstant: 44)
        ])
    }
}

// MARK: - Delegates

extension MessagesHeaderView: CKSearchBarDelegate {
    func searchBarDidBeginEditing(_ searchBar: CKSearchBar) {}
    func searchBarTextDidChange(_ searchBar: CKSearchBar, text: String) {
        delegate?.didSearchTextChange(text)
    }
    func searchBarDidCancel(_ searchBar: CKSearchBar) {}
}

extension MessagesHeaderView: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        delegate?.didTapAddButton()
    }
}
