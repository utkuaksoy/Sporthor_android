//
//  MissionTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 22.05.2025.
//

import UIKit
import ComponentKit

protocol MissionTableViewCellDelegate: AnyObject {
    func missionDetailRightButtonTapped(_ cell: MissionTableViewCell)
    func missionDetailRightButtonTappedWithModel(_ model: GetCalendarDetailTaskModel)
    func missionDetailRPEButtonTappedWithModel(_ model: GetCalendarDetailTaskModel)
    func missionDetailLocationTappedWithModel(_ model: GetCalendarDetailTaskModel)
}

extension MissionTableViewCellDelegate {
    func missionDetailRightButtonTapped(_ cell: MissionTableViewCell) {}
    func missionDetailRightButtonTappedWithModel(_ model: GetCalendarDetailTaskModel) {}
    func missionDetailRPEButtonTappedWithModel(_ model: GetCalendarDetailTaskModel) {}
    func missionDetailLocationTappedWithModel(_ model: GetCalendarDetailTaskModel) {}
}

final class MissionTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var borderView: UIView = {
        let view = UIView()
        view.setBorderWidth(1)
        view.setBorderColor(DesignKitColorName.borderSoft200.color)
        view.setCornerRadius(8)
        view.backgroundColor = DesignKitColorName.backgroundWeak100.color
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .heading06)
        return label
    }()
    
    private lazy var timeIcon: UIImageView = {
        let imageView = UIImageView(image: Asset.time.image)
        imageView.contentMode = .scaleAspectFit
        imageView.heightAnchor.constraint(equalToConstant: 20).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 20).isActive = true
        return imageView
    }()
    
    private lazy var timeLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSoft600.color,
                            font: .body04Compact)
        return label
    }()
    
    private lazy var timeStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal,
                                    distribution: .fillProportionally,
                                    alignment: .trailing,
                                    spacing: 8)
        stackView.addArrangedSubviews([timeIcon, timeLabel])
        return stackView
    }()
    
    private lazy var missionsStackView: CKStackView = {
        let stackView = CKStackView(alignment: .trailing, spacing: 12)
        return stackView
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .body04Compact)
        return label
    }()
    
    private lazy var profileImagesStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: -8)
        stackView.widthAnchor.constraint(equalToConstant: 72).isActive = true
        return stackView
    }()
    
    private lazy var groupImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.clipsToBounds = true
        imageView.setCornerRadius(12)
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        return imageView
    }()
    
    private lazy var groupLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .body04Compact)
        return label
    }()
    
    private lazy var groupHorizontalStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 8)
        stackView.addArrangedSubviews([groupImageView, groupLabel])
        return stackView
    }()
    
    private lazy var detailRightButton: CKButton = {
        let button = CKButton(delegate: self, image: Asset.chevronRight.image)
        return button
    }()
    
    private lazy var rpeButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: "RPE",
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: .clear,
                              cornerRadius: 15,
                              borderWidth: 1,
                              borderColor: DesignKitColorName.contentStrong900.color,
                              font: .bold04Compact,
                              image: Asset.blackPlus.image,
                              imageTitleSpacing: 4,
                              tag: 99)
        button.heightAnchor.constraint(equalToConstant: 30).isActive = true
        button.widthAnchor.constraint(equalToConstant: 74).isActive = true
        button.isHidden = true
        return button
    }()
    
    private lazy var bottomHorizontalStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center, spacing: 8)
        stackView.addArrangedSubviews([profileImagesStackView,
                                       groupHorizontalStackView,
                                       rpeButton])
        return stackView
    }()
    
    private lazy var locationImageView: UIImageView = {
        let imageView = UIImageView(image: Asset.pin.image)
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        return imageView
    }()
    
    private lazy var locationLabel: CKLabel = {
        let label = CKLabel(text: "Haritada Göster",
                            textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold04Compact)
        return label
    }()
    
    private lazy var locationRightImageView: UIImageView = {
        let imageView = UIImageView(image: Asset.chevronRight.image)
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        return imageView
    }()
    
    private lazy var locationStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal,
                                    distribution: .fill,
                                    alignment: .center,
                                    spacing: 8)
        stackView.addArrangedSubviews([locationImageView,
                                        locationLabel,
                                        locationRightImageView])
        stackView.isUserInteractionEnabled = true
        stackView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(didTappedLocationStackView)))
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Members
    private weak var delegate: MissionTableViewCellDelegate?
    private var model: GetCalendarDetailTaskModel?
    private var detailRightButtonWidthCons: NSLayoutConstraint!
    private var detailRightButtonHeightCons: NSLayoutConstraint!
    private var detailRightButtonTrailingCons: NSLayoutConstraint!
    
    // MARK: - Initialize
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupView()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }
    
    private func setupView() {
        selectionStyle = .none
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        
        contentView.addSubview(borderView)
        
        [titleLabel,
         timeStackView,
         missionsStackView,
         descriptionLabel,
         bottomHorizontalStackView,
         locationStackView,
         detailRightButton].forEach {
            $0.translatesAutoresizingMaskIntoConstraints = false
            borderView.addSubview($0)
        }
        
        missionsStackView.setContentHuggingPriority(.defaultHigh, for: .horizontal)
        missionsStackView.setContentCompressionResistancePriority(.defaultHigh, for: .horizontal)
        
        detailRightButtonWidthCons = detailRightButton.widthAnchor.constraint(equalToConstant: 24)
        detailRightButtonHeightCons = detailRightButton.heightAnchor.constraint(equalToConstant: 24)
        detailRightButtonTrailingCons = detailRightButton.trailingAnchor.constraint(equalTo: borderView.trailingAnchor, constant: -16)
        
        NSLayoutConstraint.activate([
            borderView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 16),
            borderView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            borderView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            borderView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            detailRightButtonTrailingCons,
            detailRightButton.centerYAnchor.constraint(equalTo: borderView.centerYAnchor),
            detailRightButtonWidthCons,
            detailRightButtonHeightCons,
            
            titleLabel.topAnchor.constraint(equalTo: borderView.topAnchor, constant: 16),
            titleLabel.leadingAnchor.constraint(equalTo: borderView.leadingAnchor, constant: 16),
            titleLabel.trailingAnchor.constraint(equalTo: missionsStackView.leadingAnchor, constant: -8),
            
            missionsStackView.topAnchor.constraint(equalTo: borderView.topAnchor, constant: 16),
            missionsStackView.trailingAnchor.constraint(equalTo: detailRightButton.leadingAnchor, constant: -8),
            
            timeStackView.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 16),
            timeStackView.leadingAnchor.constraint(equalTo: borderView.leadingAnchor, constant: 16),
            timeStackView.trailingAnchor.constraint(lessThanOrEqualTo: detailRightButton.leadingAnchor, constant: -8),
            
            descriptionLabel.topAnchor.constraint(equalTo: timeStackView.bottomAnchor, constant: 16),
            descriptionLabel.leadingAnchor.constraint(equalTo: borderView.leadingAnchor, constant: 16),
            descriptionLabel.trailingAnchor.constraint(equalTo: detailRightButton.leadingAnchor, constant: -8),
            
            bottomHorizontalStackView.topAnchor.constraint(equalTo: descriptionLabel.bottomAnchor, constant: 16),
            bottomHorizontalStackView.leadingAnchor.constraint(equalTo: borderView.leadingAnchor, constant: 16),
            bottomHorizontalStackView.trailingAnchor.constraint(equalTo: detailRightButton.leadingAnchor, constant: -8),
            
            locationStackView.topAnchor.constraint(equalTo: bottomHorizontalStackView.bottomAnchor, constant: 16),
            locationStackView.leadingAnchor.constraint(equalTo: borderView.leadingAnchor, constant: 16),
            locationStackView.trailingAnchor.constraint(equalTo: detailRightButton.leadingAnchor, constant: -8),
            locationStackView.bottomAnchor.constraint(equalTo: borderView.bottomAnchor, constant: -16)
        ])
    }
    
    private func setupMissionsStackView(missions: [(UIColor, String)]) {
        missionsStackView.removeAllArrangedSubviews()
        var rowStack: CKStackView? = nil
        for (index, mission) in missions.enumerated() {
            if index % 3 == 0 {
                rowStack = CKStackView(axis: .horizontal, alignment: .center, spacing: 12)
                missionsStackView.addArrangedSubview(rowStack!)
            }
            let tagView = CKMissionTagView()
            tagView.bind(color: mission.0, text: mission.1)
            rowStack?.addArrangedSubview(tagView)
        }
    }

    private func setupProfileImages(_ images: [UIImage]) {
        profileImagesStackView.removeAllArrangedSubviews()
        for image in images {
            let imageView = UIImageView()
            imageView.image = image
            imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
            imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
            imageView.setCornerRadius(12)
            imageView.clipsToBounds = true
            imageView.contentMode = .scaleAspectFill
            profileImagesStackView.addArrangedSubview(imageView)
        }
    }
    
    private func setupProfileImages(_ imagesUrl: [String]) {
        profileImagesStackView.removeAllArrangedSubviews()
        for image in imagesUrl {
            let imageView = UIImageView()
            imageView.setImage(with: image, placeholder: Asset.errorUserImage.image)
            imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
            imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
            imageView.setCornerRadius(12)
            imageView.clipsToBounds = true
            imageView.contentMode = .scaleAspectFill
            profileImagesStackView.addArrangedSubview(imageView)
        }
    }

    func bind(delegate: MissionTableViewCellDelegate? = nil,
              title: String = "",
              time: String = "",
              description: String = "",
              profileImages: [UIImage] = [],
              isGroup: Bool,
              groupImage: UIImage? = nil,
              groupName: String = "",
              missions: [(UIColor, String)] = []) {
        self.delegate = delegate
        titleLabel.text = title
        timeLabel.text = time
        descriptionLabel.text = description
        
        // Missions stack view setup
        setupMissionsStackView(missions: missions)
        
        if isGroup {
            profileImagesStackView.isHidden = true
            groupImageView.isHidden = false
            groupLabel.isHidden = false
            groupImageView.image = groupImage
            groupLabel.text = groupName
        } else {
            groupImageView.isHidden = true
            groupLabel.isHidden = true
            profileImagesStackView.isHidden = false
            setupProfileImages(profileImages)
        }
    }
    
    func bind(delegate: MissionTableViewCellDelegate? = nil,
              model: GetCalendarDetailTaskModel,
              isDraft: Bool = false) {
        self.delegate = delegate
        self.model = model
        titleLabel.text = model.title
        timeLabel.text = model.hour
        descriptionLabel.text = model.description
        
        let color = UIColor(hex: model.taskType?.detail ?? "#000000")
        
        setupMissionsStackView(missions: [(color, model.taskType?.name ?? "")])
        
        if let trainingGroup = model.trainingGroup {
            profileImagesStackView.isHidden = true
            groupHorizontalStackView.isHidden = false
            groupImageView.setImage(with: trainingGroup.detail)
            groupLabel.text = trainingGroup.name
            bottomHorizontalStackView.distribution = .fill
        } else if !model.users.isEmpty {
            groupHorizontalStackView.isHidden = true
            profileImagesStackView.isHidden = false
            let profileImages = model.users.map { $0.imageUrl }
            setupProfileImages(profileImages)
            bottomHorizontalStackView.distribution = .equalSpacing
        }
        
        if let taskType = model.taskType,
           (taskType.value == "685d9899450913f6cbe99bb3" || taskType.value == "685d989c450913f6cbe99bb4") {
            rpeButton.isHidden = false
        } else {
            rpeButton.isHidden = true
            if !model.users.isEmpty {
                let spacer = UIView()
                spacer.setContentHuggingPriority(.defaultLow, for: .horizontal)
                spacer.setContentCompressionResistancePriority(.defaultLow, for: .horizontal)
                bottomHorizontalStackView.addArrangedSubview(spacer)
            }
        }
        
        if isDraft {
            rpeButton.isHidden = true
            let spacer = UIView()
            spacer.setContentHuggingPriority(.defaultLow, for: .horizontal)
            spacer.setContentCompressionResistancePriority(.defaultLow, for: .horizontal)
            bottomHorizontalStackView.addArrangedSubview(spacer)
        }
        
        if !model.isOwn {
            detailRightButtonWidthCons.constant = 0
            detailRightButtonHeightCons.constant = 0
            detailRightButtonTrailingCons.constant = -8
            detailRightButton.isHidden = true
        }
        
        detailRightButton.setTag(2)
    }
    
    @objc private func didTappedLocationStackView() {
        guard let model else { return }
        delegate?.missionDetailLocationTappedWithModel(model)
    }
}

// MARK: - CKButtonDelegate
extension MissionTableViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        switch tag {
        case 0:
            delegate?.missionDetailRightButtonTapped(self)
        case 2:
            guard let model else { return }
            delegate?.missionDetailRightButtonTappedWithModel(model)
        case 99:
            guard let model else { return }
            delegate?.missionDetailRPEButtonTappedWithModel(model)
        default:
            break
        }
    }
}
