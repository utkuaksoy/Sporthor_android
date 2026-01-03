//
//  ProfileEditSelectableBranchesTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 10.04.2025.
//

import UIKit
import ComponentKit

public final class ProfileEditSelectableBranchesTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var ckSelectableBranchesView: CKSelectableBranchesView = {
        let ckSelectableBranchesView = CKSelectableBranchesView(
            tagsButtonTitleColor: DesignKitColorName.contentSoft600.color,
            tagsButtonBackgroundColor: DesignKitColorName.backgroundWeak100.color,
            tagsButtonFont: .bold04Compact,
            selectedTagsButtonTitleColor: DesignKitColorName.contentStrong900.color,
            selectedTagsButtonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            selectedTagsButtonFont: .bold04Compact,
            tagsButtonImageTitleSpacing: 4,
            addButtonImage: Asset.greyPlus.name,
            addButtonTitle: "Branş Ekle",
            addButtonTitleColor: DesignKitColorName.contentSoft600.color,
            addButtonTitleFont: .bold04Compact,
            addButtonBackgroundColor: DesignKitColorName.backgroundWeak100.color,
            addButtonImageTitleSpacing: 4,
            buttonsCornerRadius: 20
        )
        ckSelectableBranchesView.translatesAutoresizingMaskIntoConstraints = false
        return ckSelectableBranchesView
    }()
    
    // MARK: - Initialize
    public override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        contentView.addSubview(ckSelectableBranchesView)
        
        NSLayoutConstraint.activate([
            ckSelectableBranchesView.topAnchor.constraint(equalTo: contentView.topAnchor),
            ckSelectableBranchesView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            ckSelectableBranchesView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            ckSelectableBranchesView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            ckSelectableBranchesView.heightAnchor.constraint(equalToConstant: 34)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: CKSelectableBranchesViewDelegate? = nil,
              tags: [(title: String, image: String, type: String, isSelected: Bool)],
              isHiddenAddButton: Bool = false) {
        ckSelectableBranchesView.bind(delegate: delegate, tags: tags, isHiddenAddButton: isHiddenAddButton)
    }
    
    func bindForBranchModel(delegate: CKSelectableBranchesViewDelegate? = nil,
                            with model: [ProfileSummaryHighlightsBranch]?) {
        let tags = model?.map {
            return (
                title: $0.branchTitle ?? "",
                image: $0.branchImage ?? "",
                type: $0.branchId ?? "",
                isSelected: $0.isSelected ?? false
            )
        } ?? []
        ckSelectableBranchesView.bind(delegate: delegate,
                                      tags: tags,
                                      isHiddenAddButton: false)
    }
}
