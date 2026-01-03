//
//  MyTeamCoachesDetailViewController.swift
//  Sporthor
//
//  Created by derTurke on 21.07.2025.
//
//

import UIKit
import ComponentKit

final class MyTeamCoachesDetailViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: MyTeamCoachesDetailPresenterProtocol {
        get { return self.basePresenter as! MyTeamCoachesDetailPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var tableView: UITableView = {
        let tableView = UITableView(frame: .zero, style: .grouped)
        tableView.delegate = self
        tableView.dataSource = self
        tableView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 16, right: 0)
        tableView.backgroundColor = .clear
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.allowsSelection = true
        tableView.separatorStyle = .none
        tableView.removeEmptyCell()
        return tableView
    }()
    
    private lazy var deleteSubmitButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: "Şecili Antrenörü Sil",
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            font: .bold03Compact,
            tag: 1)
        button.isHidden = true
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    private var deleteSubmitButtonTopCons: NSLayoutConstraint!
    private var deleteSubmitButtonBottomCons: NSLayoutConstraint!
    private var deleteSubmitButtonHeightCons: NSLayoutConstraint!
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.navigationItem.setHidesBackButton(true, animated: false)
        if let navCon = navigationController as? CustomNavigationController {
            navCon.customDelegate = self
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        navigationController?.navigationItem.setHidesBackButton(false, animated: false)
    }
    
    // MARK: - Custom Methods
}

// MARK: - MyTeamCoachesDetailPresenterDelegate
extension MyTeamCoachesDetailViewController: MyTeamCoachesDetailPresenterDelegate {
    func prepareNavigationBar() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.isBackChevronLeft = true
            navCon.navigationBar.titleTextAttributes = [
                .foregroundColor: DesignKitColorName.contentStrong900.color,
                .font: UIFont.bold03Compact
            ]
        }
    }
    
    func prepareUI() {
        view.addSubview(tableView)
        view.addSubview(deleteSubmitButton)
        
        deleteSubmitButtonTopCons = deleteSubmitButton.topAnchor.constraint(equalTo: tableView.bottomAnchor)
        deleteSubmitButtonBottomCons = deleteSubmitButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor)
        deleteSubmitButtonHeightCons = deleteSubmitButton.heightAnchor.constraint(equalToConstant: 0)
        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            
            deleteSubmitButtonTopCons,
            deleteSubmitButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            deleteSubmitButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            deleteSubmitButtonBottomCons,
            deleteSubmitButtonHeightCons
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
    
    func changeDeleteSubmitButtonHiddenState(_ isHidden: Bool) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            if let navCon = navigationController as? CustomNavigationController {
                navCon.isRemoveTextRightBarButtonItem = true
                navCon.isTextRightBarButtonItem = (
                    text: isHidden ? presenter.model.coaches.isEmpty ? "" : "Sil" : "İptal",
                    textColor: DesignKitColorName.contentStrong900.color,
                    font: .body03Compact
                )
            }
            self.deleteSubmitButton.isHidden = isHidden
            self.deleteSubmitButtonTopCons.constant = isHidden ? 0 : 8
            self.deleteSubmitButtonBottomCons.constant = isHidden ? 0 : -16
            self.deleteSubmitButtonHeightCons.constant = isHidden ? 0 : 46
        }
    }
}

// MARK: - UITableViewDataSource
extension MyTeamCoachesDetailViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return presenter.model.trainingGroups.count
        case 1:
            return presenter.model.coaches.count
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            let cell = TrainingGroupTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.configureWithCoaches(presenter.model.trainingGroups[indexPath.row], image: presenter.model.logo)
            return cell
        case 1:
            let user = presenter.model.coaches[indexPath.row]
            let cell = ChatUserCell.dequeue(from: tableView, at: indexPath)
            cell.bind(
                name: user.name,
                role: user.username,
                imageUrl: user.imageUrl,
                isGroupSelection: presenter.isDeleted,
                isSelected: user.isSelected
            )
            return cell
        default:
            return UITableViewCell()
        }
    }
}

// MARK: - UITableViewDelegate
extension MyTeamCoachesDetailViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        presenter.didSelectRowAt(indexPath)
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        switch section {
        case 0:
            return presenter.model.trainingGroups.isEmpty ? 0 : 24
        case 1:
            return presenter.model.coaches.isEmpty ? 0 : 24
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let view = CKHeaderView(leadingCons: 16, trailingCons: -16)
        
        switch section {
        case 0:
            view.updateText("Antreman Grupları")
        case 1:
            view.updateText("Antrenörler")
        default:
            break
        }
        
        return view
    }
}

extension MyTeamCoachesDetailViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedCKButton(tag)
    }
}

extension MyTeamCoachesDetailViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        presenter.didTappedNavigationButton(type)
    }
}
