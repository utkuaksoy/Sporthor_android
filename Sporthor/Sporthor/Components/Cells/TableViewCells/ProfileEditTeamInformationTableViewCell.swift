//
//  ProfileEditTeamInformationTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 10.04.2025.
//

import UIKit
import ComponentKit

final class ProfileEditTeamInformationTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collectionView.backgroundColor = .clear
        collectionView.dataSource = self
        collectionView.delegate = self
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.showsVerticalScrollIndicator = false
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        return collectionView
    }()
    
    private lazy var infoImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.heightAnchor.constraint(equalToConstant: 18).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 18).isActive = true
        return imageView
    }()
    
    private lazy var infoLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color, numberOfLines: 0, font: .body04Compact)
        return label
    }()
    
    private lazy var infoStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .leading, spacing: 8)
        stackView.addArrangedSubviews([infoImageView, infoLabel])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private var collectionViewHeightConstraint: NSLayoutConstraint!
    
    // MARK: - Members
    private var model: TeamsDataModel?
    
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
        contentView.addSubview(collectionView)
        contentView.addSubview(infoStackView)
        
        collectionViewHeightConstraint = collectionView.heightAnchor.constraint(equalToConstant: 100)
        collectionViewHeightConstraint.isActive = true
        
        NSLayoutConstraint.activate([
            collectionView.topAnchor.constraint(equalTo: contentView.topAnchor),
            collectionView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            collectionViewHeightConstraint,
            
            infoStackView.topAnchor.constraint(equalTo: collectionView.bottomAnchor, constant: 16),
            infoStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            infoStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            infoStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -16)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(with model: ProfileSummaryTeamInfo?, infoImage: String) {
        guard let model = model else { return }

        var tempTeamsDataModel = TeamsDataModel()
        tempTeamsDataModel.title = model.title

        tempTeamsDataModel.teams = model.teams?.compactMap { teamInfo in
            let imageUrl = teamInfo.teamImage ?? ""
            return TeamModel(teamLogoImageUrl: imageUrl, teamName: teamInfo.teamName)
        }
        
        if tempTeamsDataModel.teams?.isEmpty ?? false {
            collectionViewHeightConstraint.constant = 60
            collectionViewHeightConstraint.isActive = true
        }

        self.model = tempTeamsDataModel
        infoImageView.image = UIImage(named: infoImage)
        infoLabel.text = model.info
        collectionView.reloadData()
    }
}

extension ProfileEditTeamInformationTableViewCell: UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = CurrentTeamsContainerCell.dequeue(from: collectionView, at: indexPath)
        if let model = model {
            cell.configure(with: model, at: indexPath)
        }
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: collectionView.frame.size.width, height: model?.teams?.isEmpty ?? false ? 60: 100)
    }
}
