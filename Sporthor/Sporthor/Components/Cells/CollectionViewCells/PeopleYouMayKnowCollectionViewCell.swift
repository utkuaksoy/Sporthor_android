//
//  PeopleYouMayKnowCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 12.03.2025.
//

import UIKit
import ComponentKit

// MARK: - PeopleYouMayKnowCollectionViewCellDelegate
protocol PeopleYouMayKnowCollectionViewCellDelegate: AnyObject {
    func didTappedProfileImage()
    func didTappedFollowButton()
}

extension PeopleYouMayKnowCollectionViewCellDelegate {
    func didTappedProfileImage() {}
    func didTappedFollowButton() {}
}

final class PeopleYouMayKnowCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var profileView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = .clear
        view.heightAnchor.constraint(equalToConstant: 72).isActive = true
        view.widthAnchor.constraint(equalToConstant: 72).isActive = true
        return view
    }()
    
    private lazy var profileImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.layer.cornerRadius = 36
        imageView.clipsToBounds = true
        imageView.heightAnchor.constraint(equalToConstant: 72).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 72).isActive = true
        imageView.isUserInteractionEnabled = true
        imageView.addGestureRecognizer(UITapGestureRecognizer(target: self,
                                                              action: #selector(didTappedProfileImage(_:))))
        return imageView
    }()
    
    private lazy var teamView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = .white
        view.layer.cornerRadius = 13
        view.clipsToBounds = true
        view.heightAnchor.constraint(equalToConstant: 26).isActive = true
        view.widthAnchor.constraint(equalToConstant: 26).isActive = true
        return view
    }()
    
    private lazy var teamImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.layer.cornerRadius = 13
        imageView.clipsToBounds = true
        imageView.heightAnchor.constraint(equalToConstant: 26).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 26).isActive = true
        return imageView
    }()
    
    private lazy var nameLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            textAlignment: .center,
                            font: .bold04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var button: CKButton = {
        let button = CKButton(delegate: self,
                              cornerRadius: 15,
                              borderWidth: 1,
                              font: .bold04Compact)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.heightAnchor.constraint(equalToConstant: 30).isActive = true
        return button
    }()
    
    // MARK: - Members
    private weak var delegate: PeopleYouMayKnowCollectionViewCellDelegate?
    
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
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        contentView.layer.cornerRadius = 8
        contentView.layer.borderWidth = 1
        contentView.layer.borderColor = DesignKitColorName.borderSoft200.color.cgColor
        
        contentView.addSubview(profileView)
        profileView.addSubview(profileImageView)
        profileView.addSubview(teamImageView)
        contentView.addSubview(nameLabel)
        contentView.addSubview(button)
        
        NSLayoutConstraint.activate([
            
            profileView.centerXAnchor.constraint(equalTo: contentView.centerXAnchor),
            profileView.topAnchor.constraint(equalTo: contentView.topAnchor,
                                             constant: 15),
            
            profileImageView.topAnchor.constraint(equalTo: profileView.topAnchor),
            profileImageView.leadingAnchor.constraint(equalTo: profileView.leadingAnchor),
            profileImageView.trailingAnchor.constraint(equalTo: profileView.trailingAnchor),
            profileImageView.bottomAnchor.constraint(equalTo: profileView.bottomAnchor),
            
            teamImageView.bottomAnchor.constraint(equalTo: profileView.bottomAnchor),
            teamImageView.trailingAnchor.constraint(equalTo: profileView.trailingAnchor),
            
            nameLabel.topAnchor.constraint(equalTo: profileImageView.bottomAnchor,
                                           constant: 8),
            nameLabel.leadingAnchor.constraint(equalTo: profileImageView.leadingAnchor,
                                               constant: 8),
            nameLabel.trailingAnchor.constraint(equalTo: profileImageView.trailingAnchor,
                                                constant: -8),
            
            button.bottomAnchor.constraint(equalTo: contentView.bottomAnchor,
                                           constant: -8),
            button.leadingAnchor.constraint(equalTo: contentView.leadingAnchor,
                                            constant: 8),
            button.trailingAnchor.constraint(equalTo: contentView.trailingAnchor,
                                             constant: -8)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: PeopleYouMayKnowCollectionViewCellDelegate? = nil,
              model: SuggestionModel,
              backgroundColor: UIColor = .clear,
              isURL: Bool = false) {
        contentView.backgroundColor = backgroundColor
        self.delegate = delegate
        if isURL {
            profileImageView.setImage(with: model.profileImage)
            teamImageView.setImage(with: model.teamImage)
        } else {
            profileImageView.image = UIImage(named: model.profileImage ?? "")
            teamImageView.image = UIImage(named: model.teamImage ?? "")
        }
        
        if let teamImage = model.teamImage, !teamImage.isEmpty {
            teamImageView.setBorderWidth(2)
            teamImageView.setBorderColor(.white)
        }
        
        nameLabel.text = model.name
        button.setBackgroundColor(model.isFollow ? DesignKitColorName.contentStrong900.color : .clear)
        button.setBorderColor(model.isFollow ? .clear : DesignKitColorName.contentStrong900.color)
        button.setTitleColor(model.isFollow ? .white : DesignKitColorName.contentStrong900.color)
        button.setTitle(model.isFollow ? "Takip Ediliyor" : "Takip Et")
    }
    
    @objc private func didTappedProfileImage(_ sender: UITapGestureRecognizer) {
        delegate?.didTappedProfileImage()
    }
}

// MARK: - CKButtonDelegate
extension PeopleYouMayKnowCollectionViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        delegate?.didTappedFollowButton()
    }
}
