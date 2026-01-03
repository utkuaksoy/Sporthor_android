//
//  EditPersonAndTechnicalStaffTrainingGroupViewController.swift
//  Sporthor
//
//  Created by derTurke on 31.10.2025.
//
//

import UIKit
import ComponentKit

final class EditPersonAndTechnicalStaffTrainingGroupViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: EditPersonAndTechnicalStaffTrainingGroupPresenterProtocol {
        get { return self.basePresenter as! EditPersonAndTechnicalStaffTrainingGroupPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var ckImageTitleView: CKImageTitleView = {
        let ckImageTitleView = CKImageTitleView()
        ckImageTitleView.translatesAutoresizingMaskIntoConstraints = false
        return ckImageTitleView
    }()
    
    private lazy var ckSegmentedControl: CKSegmentedControl = {
        let segmentedControl = CKSegmentedControl(delegate: self, titles: ["Oyuncular", "Teknik Kadro"])
        segmentedControl.translatesAutoresizingMaskIntoConstraints = false
        return segmentedControl
    }()
    
    private lazy var topStackView: CKStackView = {
        let stackView = CKStackView(spacing: 16)
        stackView.addArrangedSubviews([ckImageTitleView, ckSegmentedControl])
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = .init(top: 16, left: 16, bottom: 0, right: 16)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorInset = .init(top: 0, left: 16, bottom: 0, right: 16)
        tableView.separatorColor = DesignKitColorName.borderSoft200.color
        tableView.contentInset = .init(top: 16, left: 0, bottom: 16, right: 0)
        tableView.backgroundColor = .clear
        tableView.translatesAutoresizingMaskIntoConstraints = false
        return tableView
    }()
    
    private lazy var addButton: CKButton = {
        let button = CKButton(
            delegate: self,
            titleColor: DesignKitColorName.contentStrong900.color,
            cornerRadius: 23,
            borderWidth: 1,
            borderColor: DesignKitColorName.borderStrong900.color,
            font: .bold03Compact
        )
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var continueButton: CKButton = {
        let ckButton = CKButton(delegate: self,
                                title: "Anasayfa",
                                titleColor: DesignKitColorName.contentStrong900.color,
                                buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                                cornerRadius: 23,
                                font: .bold03Compact,
                                tag: 1)
        ckButton.heightAnchor.constraint(equalToConstant: 46).isActive = true
        ckButton.translatesAutoresizingMaskIntoConstraints = false
        return ckButton
    }()
    
    private lazy var bottomStackView: CKStackView = {
        let stackView = CKStackView(spacing: 12)
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = .init(top: 0, left: 16, bottom: 32, right: 16)
        stackView.addArrangedSubviews([addButton, continueButton])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if let navCon = navigationController as? CustomNavigationController {
            navCon.customDelegate = self
        }
    }
    
    // MARK: - Custom Methods
    
}

// MARK: - EditPersonAndTechnicalStaffTrainingGroupPresenterDelegate
extension EditPersonAndTechnicalStaffTrainingGroupViewController: EditPersonAndTechnicalStaffTrainingGroupPresenterDelegate {
    func prepareNavigationBar() {
        if ApplicationContext.shared.isLogin,
           let navCon = navigationController as? CustomNavigationController {
            navCon.isBackChevronLeft = true
        }
    }
    
    func prepareUI() {
        view.addSubview(topStackView)
        view.addSubview(tableView)
        view.addSubview(bottomStackView)
        
        NSLayoutConstraint.activate([
            topStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            topStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            topStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            
            tableView.topAnchor.constraint(equalTo: topStackView.bottomAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            
            bottomStackView.topAnchor.constraint(equalTo: tableView.bottomAnchor),
            bottomStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            bottomStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            bottomStackView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor)
        ])
    }
    
    func prepareCKImageTitleView(image: String,
                                 title: String,
                                 rightImage: UIImage,
                                 backgroundImage: UIImage?) {
        ckImageTitleView.bind(
            delegate: self,
            image: image,
            title: title,
            titleColor: DesignKitColorName.contentWhite0.color,
            titleFont: .body05MediumCompact,
            rightImage: rightImage,
            backgroundImage: backgroundImage
        )
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            tableView.reloadData()
        }
    }
    
    func prepareAddButtonTitle(_ title: String) {
        addButton.setTitle(title)
    }
    
    func hiddenTab() {
        ckSegmentedControl.isHidden = true
    }
}

// MARK: - CKImageTitleViewDelegate
extension EditPersonAndTechnicalStaffTrainingGroupViewController: CKImageTitleViewDelegate {
    func didTappedCKImageTitleView(_ tag: Int) {
        presenter.didTappedCKImageTitleView(tag)
    }
}

// MARK: - CKSegmentedControlDelegate
extension EditPersonAndTechnicalStaffTrainingGroupViewController: CKSegmentedControlDelegate {
    func segmentedControl(_ control: CKSegmentedControl, didSelect index: Int) {
        presenter.segmentedControl(didSelect: index)
    }
}

// MARK: - UITableViewDataSource
extension EditPersonAndTechnicalStaffTrainingGroupViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch presenter.viewType {
        case .person:
            return presenter.model?.users.isEmpty ?? true ? 1 : presenter.model?.users.count ?? 0
        case .technicalStaff:
            return presenter.model?.coaches.isEmpty ?? true ? 1 : presenter.model?.coaches.count ?? 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let list = presenter.viewType == .person ? presenter.model?.users : presenter.model?.coaches,
              !list.isEmpty else {
            let cell = EmptyTableViewCell.dequeue(from: tableView, at: indexPath)
            let description = presenter.viewType == .person ? "Antreman grubunuzda sporcu bulunmamaktadır. Sporcu eklemek için \"Yeni Sporcu Ekle\" butonuna tıklayınız." : "Antreman grubunuzda antrenör bulunmamaktadır. Antrenör eklemek için \"Yeni Antrenör Ekle\" butonuna tıklayınız."
            cell.bind(image: Asset.infoAlert.image, description: description)
            return cell
        }
        
        let model = list[indexPath.row]
        
        let cell = ImageTitleInfoRightImageTableViewCell.dequeue(from: tableView, at: indexPath)
        cell.configure(image: model.imageUrl,
                       title: model.name,
                       info: model.summary,
                       rightImage: model.id == ApplicationContext.shared.userId ? nil : presenter.viewType == .technicalStaff ? Asset.chevronRightGrey.image : nil)
        return cell
    }
}

// MARK: - UITableViewDelegate
extension EditPersonAndTechnicalStaffTrainingGroupViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        presenter.didSelectRowAt(indexPath)
    }
    
    func tableView(_ tableView: UITableView, trailingSwipeActionsConfigurationForRowAt indexPath: IndexPath) -> UISwipeActionsConfiguration? {
        guard let list = presenter.viewType == .person ? presenter.model?.users : presenter.model?.coaches,
              !list.isEmpty,
              ApplicationContext.shared.userId != list[indexPath.row].id else {
            return nil
        }
        let deleteAction: UIContextualAction = UIContextualAction(style: .destructive, title: "") { [weak self] _, _, completion in
            guard let self else { return }
            
            self.presenter.deletePersonTrainingGroup(indexPath)
            completion(true)
        }
        deleteAction.image = Asset.trashIcon.image
        deleteAction.backgroundColor = DesignKitColorName.errorBase500.color
        
        let configuration = UISwipeActionsConfiguration(actions: [deleteAction])
        configuration.performsFirstActionWithFullSwipe = true
        return configuration
    }
}

// MARK: - CKButtonDelegate
extension EditPersonAndTechnicalStaffTrainingGroupViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedCKButton(tag)
    }
}

extension EditPersonAndTechnicalStaffTrainingGroupViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        presenter.didTappedNavigationButton(type)
    }
}
