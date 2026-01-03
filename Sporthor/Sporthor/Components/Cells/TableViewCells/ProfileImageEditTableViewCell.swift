//
//  ProfileImageEditTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 9.04.2025.
//

import UIKit
import ComponentKit

protocol ProfileImageEditTableViewCellDelegate: AnyObject {
    func didTappedUpdateProfileImageButton()
}

final class ProfileImageEditTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(alignment: .center, spacing: 16)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var profileImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.widthAnchor.constraint(equalToConstant: 72).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 72).isActive = true
        imageView.setCornerRadius(36)
        imageView.clipsToBounds = true
        imageView.contentMode = .scaleAspectFill
        return imageView
    }()
    
    private lazy var nameLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .heading07)
        return label
    }()
    
    private lazy var updateButton: CKButton = {
        let button = CKButton(delegate: self, titleColor: DesignKitColorName.blue600.color, font: .bold04Compact)
        return button
    }()
    
    private lazy var seperatorView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.borderSoft200.color
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Members
    private weak var delegate: ProfileImageEditTableViewCellDelegate?
    
    // MARK: - Initialize
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        
        stackView.addArrangedSubviews([profileImageView, nameLabel, updateButton])
        contentView.addSubview(stackView)
        contentView.addSubview(seperatorView)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 32),
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            
            seperatorView.topAnchor.constraint(equalTo: stackView.bottomAnchor, constant: 24),
            seperatorView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            seperatorView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            seperatorView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            seperatorView.heightAnchor.constraint(equalToConstant: 1)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: ProfileImageEditTableViewCellDelegate? = nil,
              image: String,
              name: String = "",
              buttonTitle: String,
              isHiddenBorderView: Bool = false) {
        self.delegate = delegate
        profileImageView.setImage(with: image, placeholder: .errorUserImage)
        nameLabel.text = name
        nameLabel.isHidden = name.isEmpty
        updateButton.setTitle(buttonTitle)
        updateButton.isHidden = buttonTitle.isEmpty
        seperatorView.isHidden = isHiddenBorderView
    }
}

extension ProfileImageEditTableViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        delegate?.didTappedUpdateProfileImageButton()
    }
}
