//
//  SearchingCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 19.03.2025.
//

import UIKit
import ComponentKit

protocol SearchingCollectionViewCellDelegate: AnyObject {
    func deletePastSearchItem(with model: SearchHistory)
}

extension SearchingCollectionViewCellDelegate {
    func deletePastSearchItem(with model: SearchHistory) {}
}

final class SearchingCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var imageView: CKStoryProfileView = {
        let imageView = CKStoryProfileView(imageSize: 48)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(spacing: 2)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var fullnameLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold04Compact)
        return label
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSoft600.color,
                            numberOfLines: 0,
                            font: .body06Compact)
        return label
    }()
    
    private lazy var deleteImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.isUserInteractionEnabled = true
        imageView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(deleteItemClicked)))
        return imageView
    }()
    
    // MARK: - Members
    private weak var delegate: SearchingCollectionViewCellDelegate?
    private var pastSearchModel: SearchHistory?
    
    // MARK: - Initializers
    override init(frame: CGRect) {
        super.init(frame: frame)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        contentView.addSubview(imageView)
        stackView.addArrangedSubviews([fullnameLabel, descriptionLabel])
        contentView.addSubview(stackView)
        contentView.addSubview(deleteImageView)
        
        NSLayoutConstraint.activate([
            imageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            imageView.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            imageView.widthAnchor.constraint(equalToConstant: 56),
            imageView.heightAnchor.constraint(equalToConstant: 56),
            
            stackView.leadingAnchor.constraint(equalTo: imageView.trailingAnchor, constant: 4),
            stackView.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            
            deleteImageView.leadingAnchor.constraint(equalTo: stackView.trailingAnchor, constant: 4),
            deleteImageView.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            deleteImageView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            deleteImageView.widthAnchor.constraint(equalToConstant: 18),
            deleteImageView.heightAnchor.constraint(equalToConstant: 18)
        ])
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        imageView.setImage(nil)
        imageView.setCornerRadius(0)
        imageView.setBorderColor(.clear)
        imageView.setBorderWidth(0)
        fullnameLabel.text = ""
        descriptionLabel.text = ""
        deleteImageView.isHidden = true
    }
    
    // MARK: - Custom Methods
    
    @objc private func deleteItemClicked() {
        if let pastSearchModel {
            delegate?.deletePastSearchItem(with: pastSearchModel)
        }
    }
    
    func bind(delegate: SearchingCollectionViewCellDelegate? = nil,
              model: SearchList) {
        self.delegate = delegate
        imageView.setImage(url: model.image, placeholder: .errorUserImage)
        fullnameLabel.text = model.name
        descriptionLabel.text = model.attribute
        deleteImageView.isHidden = !model.isPast
        deleteImageView.image = Asset.closeGrey.image
    }
    
    func searchingPlaceholder(delegate: SearchingCollectionViewCellDelegate? = nil,
                              searchText: String) {
        self.delegate = delegate
        imageView.setImage(Asset.searchbarSearch.image, contentMode: .center)
        imageView.setCornerRadius(28)
        imageView.setBorderColor(DesignKitColorName.borderSoft200.color)
        imageView.setBorderWidth(1)
        fullnameLabel.text = searchText
    }
    
    func bindPastSearch(delegate: SearchingCollectionViewCellDelegate? = nil,
                        model: SearchHistory) {
        self.delegate = delegate
        self.pastSearchModel = model
        if let searchUser = model.searchUser {
            imageView.setImage(url: searchUser.image, placeholder: .errorUserImage)
            fullnameLabel.text = searchUser.name
            descriptionLabel.text = searchUser.summary
        } else {
            searchingPlaceholder(delegate: delegate,
                                 searchText: model.searchQuery ?? "")
        }
        deleteImageView.isHidden = false
        deleteImageView.image = Asset.closeGrey.image
    }
    
    
}
